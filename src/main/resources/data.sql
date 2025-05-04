-- 1. 사용자
INSERT INTO users (id, email, password, username, address, phone_number)
VALUES (1, 'test@auta.com', '$2b$12$fhouvPYQIS9ORwTxUvocDu1aK./W2vnueD/FoE7Y4UIZnlHZD0/FG', '테스트유저', '서울시 강남구',
        '010-1234-5678');

-- PROJECTS
INSERT INTO projects (id, user_id, figma_url, root_figma_page, service_url, project_name, description,
                      project_created_date, project_end, project_status, test_execute_time, test_rate,
                      created_date_time, modified_date_time)
VALUES (1, 1, 'https://figma.com/proj1', '홈페이지', 'https://auta.com', 'AUTA 테스트 프로젝트', '설명입니다', CURRENT_DATE,
        CURRENT_DATE + 30, 'NOT_STARTED', CURRENT_TIME, 85, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- PAGES
INSERT INTO pages (id, project_id, page_name, page_base_url, created_date_time, modified_date_time)
VALUES (1, 1, '홈', '/home', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 1, '회원가입', '/signup', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 1, '로그인', '/login', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 1, '대시보드', '/dashboard', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- TESTS
INSERT INTO tests (id, project_id, page_id, test_status, test_type, fail_reason, trigger_selector, expected_destination,
                   actual_destination, trigger, expected_action, actual_action, component_name)
VALUES (1, 1, 1, 'PASSED', 'ROUTING', NULL, '#start-btn', '/signup', '/signup', 'start 버튼 클릭', '이동', '이동', '시작버튼'),
       (2, 1, 1, 'PASSED', 'INTERACTION', NULL, '#submit', NULL, NULL, '회원가입 제출', '서버 호출', '서버 호출', '제출버튼'),
       (3, 1, 1, 'FAILED', 'ROUTING', '잘못된 경로로 이동', '#login-btn', '/dashboard', '/error', '로그인 버튼', '이동', '에러',
        '로그인버튼'),
       (4, 1, 1, 'PASSED', 'COMPONENT', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '알림아이콘');