# tdd-java-practice

- 테스트 케이스를 먼저 작성하여 top-down 방식으로 진행.
- 분산환경 고려 안함.


API Specs
- PATCH  `/point/{id}/charge` : 포인트를 충전한다.
- PATCH `/point/{id}/use` : 포인트를 사용한다.
- GET `/point/{id}` : 포인트를 조회한다.
- GET `/point/{id}/histories` : 포인트 내역을 조회한다.

- 잔고가 부족할 경우, 포인트 사용은 실패.
- 동시에 여러 건의 포인트 충전, 이용 요청이 들어올 경우 순차적으로 처리. (동시성)