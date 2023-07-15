# Trouble Shooting
프로젝트를 진행하면서 발생한 문제점들과 해결법 서술합니다.

## h2 DB 사용하여 테스트 시 "Error executing DDL" 발생
테스트 코드를 수행할 때는 h2 DB를 사용하고자 하였습니다. 하지만 Entity로 사용하던 User가 h2에
예약어로 존재하였습니다. 이를 해결하고자 다음과 같이 수정하였습니다.
- User -> Member 로 수정

지금은 개발의 초기 단계여서 간단한 코드 수정으로 수정이 가능했습니다. 하지만 만약 초기 단계가 아니라면 다른 수를 찾아볼 것 같습니다.

## CustomException 이 발생하지 않고 email UK에 의해서 예외 처리 발생
회원가입 시에 email 의 중복을 방지하기 위해서 그에 대한 예외처리를 할 수 있도록 했던 코드 부분이
잘 작동하지 않았다.
throw 를 붙이면 에러가 나서 "수정 전"과 같이 하면 작동될 것 같아 넘어간 코드 부분이었다.
이 부분을 잘 찾아보니 new만 해주면 예외를 생성하기만 하고 throw를 해주지 않아 발생하지 않는다고 하였다.
해당 부분은 중괄호로 감싸주고 throw를 붙여 해결하였다.

수정 전

    memberRepository.findByEmail(signUpForm.getEmail())
        .ifPresent(a -> new CustomException(ErrorCode.ALREADY_EXIST_ACCOUNT));

수정 후

    memberRepository.findByEmail(signUpForm.getEmail())
        .ifPresent(a -> {
            throw new CustomException(ErrorCode.ALREADY_EXIST_ACCOUNT);
    });

## ExpiredJwtException 발생 시 ExceptionHandler 작동 안 함
해당 건은 어플리케이션의 구조를 잘 몰랐던 이슈였습니다. 
ExceptionHandler의 경우 Spring에서 발생하는 예외는 잡아주지만 그 이전 단인 Filter에서 발생했기 때문에
예외가 발생해도 ExceptionHandler가 작동이 안 됐던 것입니다.
해당 건은 Excpetion이 발생했을 때 catch로 감싸준 뒤 response를 설정해주는 방식으로 하여 해결하였습니다.

## Cacheable 동작 안 함
DayClass의 별점을 가져올 때 DayClass 내부에 Review로 부터 평균 별점을 가져오는 매서드를 구현했지만 캐싱이 되지 않았다.
Spring의 @Cacheable 동작은 AOP를 이용하기 때문에 내부 매서드를 사용할 땐 작동하지 않는다.
이 문제는 DayClassService -> ReviewService로 옮겨 외부 매서드로 만들어 작동시켜 해결했다.

## 회원가입 직후 로그인 안 됨
회원가입 직후에 로그인 시 정상적으로 입력했는데도 회원가입한 멤버를 찾지 못하는 예외가 발생했다.
이 에러는 회원가입 시에 이메일 중복 확인용으로 DB를 조회하는데 이 때 캐시 메모리에 저장된 것을 조회하고
없다고 예외가 발생하는 것이었다.
이 문제는 회원가입 시에 발생하는 null에 대해서는 캐시 메모리에 저장하지 않도록 설정하여 해결하였다.

수정 전

    @Cacheable(value = "member", key = "#email", unless = "#result == null")

수정 후

    @Cacheable(value = "member", key = "#email", unless = "#result == null")

## 엘라스틱서치로 인하여 데이클래스 생성 시와 수정 시에 검색 타입이 text로 검색 되어 중복 오류 발생
DayClass 생성 시와 수정 시에 데이클래스 이름 중복을 필터링하기 위해서 storeId와 dayClassName로
조회하는 로직이 있습니다. 해당 로직에서 dayClassName을 검색 시에 text type으로 검색되기 때문에
중복된 이름이 아닌데도 중복으로 예외가 발생했습니다.
해당 오류는 dayClassName을 2개 만들어서 Type을 text, keyword로 관리하도록 수정했습니다.
중복 검색 시에는 keyword를 사용하며 사용자가 검색 시에는 type을 사용하여 엘라스틱 서치를 지원합니다.

    @Field(type = FieldType.Text)
    private String dayClassNameText;
    
    @Field(type = FieldType.Keyword)
    private String dayClassNameKeyword;