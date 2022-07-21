# Chapter 4. 문서 관리 시스템

###  목표

1. 상속 관계
- 리스코프 치환 원칙
- 상속보다 조합 원칙

2. __문서관리 시스템__ 요구 사항
   - 기존 환자 정보 파일을 읽기
   - 색인 추가
   - 검색할 수 있는 형태의 정보로 변환
   - 4가지 형식의 문서에 대한 개별 처리
   - 확장 가능성

```java
class example {
    public void example1 () {
       switch(extension) {
          case "letter" :
             // 우편물 임포트
             break;
          case "report" :
             // 레포트 임포트 코드
             break;
          case "jpg" :
             //이미지 임포트 코드
             break;
          default :
             throw new UnknownFileTypeException("For file: " + path);
       }
    }
}
```

- 다른 종류의 파일을 추가할 때마다 switch 문에 다른 항목을 추가해 구현해야 하기 때문에 확장성이 부족함
- 추가로 구현하게 되면서 메서드가 길어지며 읽기 어려워 질 수 있음.

- strongly typed (엄격한 자료형)
  - 함수/메서드에 전달하는 객체나 값의 타입이 프로그래머의 의도와 다를 때, 컴파일러가 에러를 내거나 컴파일을 거부하는 식으로 타입을 맞춤
  - 기존의 String type의 path 입력 방식보다 File.io type의 입력 형식으로 받게되면 오류 발생 범위를 최소화 할 수 있음. 
  - 공개 API 에서는 File 을 사용하지 않는 이유는 인터페이스로 감싸진 상태이기 때문에 어떤 파일 형식인지 알 수 없기 때문에 String으로 사용



## Document class

document class에 계층을 추가한다 ?
문서의 각 서브클래스가 전용 document 클래스를 갖지 않도록 Document를 범용으로 사용하기 위해 본 예제에서는 계층을 따로 두지 않았다. 각 문서는 
Document와 관련된 동작을 포함하지 않는다. 또한, 클래스 계층으로 인한 이득이 없다면 계층을 추가할 필요 없음 (KISS)

### Map vs VO
  - Map 장점 : 
    - 빠르게 개발, 수정이 용이함
    - Map 내부 자료값은 Debug 유틸로 확인가능하다 - 누군가의 주장
    - 리턴 형태가 다를 때마다 다른 VO를 만드는 것보다 Map을 사용하면 확장성이 있다 - 누군가의 주장
  - Map 단점 : 
    - 객체로 정의되지 않기 때문에 재사용성이 떨어짐
    - 필드명 타이핑 필요함
    - dataset 규격을 파악하기 어려움 
    - 타입 캐스팅 오류 가능성
    - 여러 사람이 같이 유지보수 할 시 커뮤니케이션이 어려움
  - VO 장점 : 
    - 해당 로직 안에서 로직 별로 가지고 있는 dataset 을 쉽게 파악할 수 있음
    - dataset 의 정의 상태에 따라 영역이나 의도를 보다 명확하게 파악 가능
  - VO 단점 : 
    - dataset 변경이 필요하면 dto 를 일일이 수정해줘야 함
    - 충분한 고민 없는 필드 추가와 명확한 목적성 설정 없이 데이터 운반체로만 활용하다보면 custom map이 될 수 있음
    - 유지보수 시 기능이 추가되며 dto를 명확하게 관리하기 보다 어려워짐


### [Document class code](Document.java)
- Document class를 생성하여 데이터의 사용을 규제할 수 있으며 오류의 발생 범위를 줄일 수 있다
- Importer에서 new Document 를 통해 불변성을 가진 Document 객체를 생성, 불변의 Document (외부에서 접근할 수 없는) 객체를 생성하기 때문에
  오류의 발생 범위를 Document를 생성해내는 Importer class들로 축소할 수 있음.
- 뭔데 Map<String,String>이지..
  - Importer의 종류와 관계없이 모든 속성이 아주 일반적인 형식을 갖도록 만들기 위함
  - 그리고.. 더 나아간 기능은 사실 문서 관리 시스템에 필요없기 때문에 가장 간단한 형태로 만듦~

### [Importer interface](Importer.java)

- importFile : file 읽어오기
- Implement Importer interface
   - [ImageImporter](ImageImporter.java)



### [DocumemtManagementSystem](DocumentManagementSystem.java)

- void importFile
- List<Document> contents
- List<Document> search(String str) : str을 Query 형식 ( patient : Joe,body:Diet Coke ) 으로 파싱

## 리스코프 치환 원칙(LSP)


- S : SRP (단일 책임의 원칙)
- O : OCP (개방폐쇄의 원칙)
- __L : LSP (리스코프 치환 원칙)__
- I : ISP (인터페이스 분리 원칙)
- D : DIP (의존성역전의 원칙)

> "서브 타입은 언제나 기반 타입으로 교체할 수 있어야 한다"
> 
> 다형성을 통한 확장성 획득을 목표로 함.

example 1.

```
-----------       --------------
| Billing |  ->   |  License   |
|         |       | + calcFree |
-----------       --------------
                         ^
                         |
               ---------------------
              |                     |
         -------------           ------------
         |  personal  |          | Business  |
         |  License   |          |  License  |
         --------------          ------------
```

example 2. 위반
```
-----------       ------------------
| User    |  ->   |  Rectangle     |
|         |       | + setH, + setW |
-----------       ------------------
                         ^
                         |
                         |                   
                  -------------           
                  |  Square    |          
                  |  + setSide |          
                  --------------  
```

- 하위 형식에서 선행조건을 더할 수 없음
  - Importer가 문서의 크기를 제한하지 않았다면 Importer를 상속받는 ReportImporter 등의 클래스들도 제한할 수 없음
- 하위 형식에서 후행조건을 약화시킬 수 없음
  - 후행조건은 어떤 코드를 실행한 다음에 만족해야하는 규칙....
  - ????
- 슈퍼형식의 불변자는 하위 형식에서 보존됨

## 기존 코드의 확장

- __접두어__ 를 포함하는 행을 찾는 범용 메서드 구현
- Amount : , Patient : , Dear 뒤에 나오는 값 매핑 구현
- [TextFile](TextFile.java)
  - [ReportImporter](ReportImporter.java)
  - [InvoiceImporter](InvoiceImporter.java)
  - [LetterImporter](LetterImporter.java)

- Report, Invoice, Letter 세가지 문서 형식에서 접미어를 찾는 메서드가 필요함

1. 유틸리티 클래스 사용
   - ImportUtil 클래스를 만들어 여러 임포트에서 공유하는 기능 구현
   - 유틸리티 크래스는 보통 한 의무나 개념과는 상관없는 다양한 코드의 모음으로 귀결되기 
   때문에 `갓 클래스`의 모습을 띄는 경우가 많으므로 지양해야한다. 
2. 상속 사용
   - TextImporter 클래스를 상속
   - TextImporter 클래스에 모든 공통 기능을 구현하고 서브클래스에서는 공통 기능을 재사용한다
   - 응용 프로그램은 수정과 변화가 잦기 때문에 상속이 쉽게 깨질 수 있어 일반적으로 상속 관계로 코드를 
   재사용하는 것은 좋은 방법이 아니다.
   - 변화를 추상화하는 것이 필요함
3. 도메인 클래스 사용
   - 도메인 클래스로 텍스트 파일을 모델링하는 방법
   - 텍스트 추출 로직이 Letter Import와 Invoice Importer에서 공유되는 것이 확인되어 텍스트를 추출하는 
   TextFile을 만들어 관리함
   - 동일하게 동작하는 기능인 `Prefix 찾아 attribute를 세팅`하는 로직을 TextFile에 위임
   - Predicate를 사용하여 Line 종료 조건식을 넘겨주어 addLines를 일반화함