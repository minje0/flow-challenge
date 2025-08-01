# flow-challenge
파일 확장자 차단 

# 확장자 차단 기능 – 추가 고려 사항

## 커스텀 확장자 중복 방지

- 이미 등록된 확장자를 다시 추가할 경우 중복 저장을 방지함.
- 서버 측에서도 `unique` 제약 또는 로직으로 중복 추가 방지 처리 예상됨.
- 예: `sh`를 추가한 뒤 다시 `sh`를 추가하면 서버 오류 메시지 반환 및 alert 처리.


## 커스텀 확장자 → 커스텀 고정 확장자 전환

- 커스텀 확장자의 "v" 버튼을 누르면, 해당 확장자는 커스텀 고정 확장자 영역으로 이동.
- DB에는 blockTypeCd를 `03`으로 갱신하는 방식.
- 이동 후 기존 커스텀 태그는 즉시 제거되고 고정 UI로 재생성됨.


## 확장자 설명 추가

- 확장자에 대한 설명을 입력받아 확장자 텍스트 마우스 오버 시 설명 표출


## 커스텀 확장자 수량 실시간 갱신

- 커스텀 확장자 수를 하단에 "n/200" 형태로 표시.
- 삭제 또는 추가 시 즉시 UI 반영.


## 키보드 Enter 입력으로 확장자 추가

- 사용자 편의를 위해 Enter 키를 누르면 "+추가"와 동일한 동작 수행.
- 입력 필드에 포커스가 있는 상태에서 Enter 키 처리 이벤트 바인딩.

