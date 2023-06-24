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
