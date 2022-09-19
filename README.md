# Spring Boot Study 2022

---

## Spring boot 

- 스프링 생태계를 기반으로 한 개발플랫폼이다.
- 단독 실행 가능한 스프링 애플리케이션을 생성
- 톰캣, 제티 혹은 언더토우를 내장할 수 있다, war 파일로 배포할 필요가 없다
- 자동구성을 제공하는 'starter' 의존성을 제공하여 빌드구성이 단순하다.
- 필요에 따라 스프링과 서드파티 라이브러리를 자동구성할 수 있다.
- 운영에서 바로 사용가능한 지표를 제공하고 , 헬스체크 외부구성 기능을 제공한다.
- xml 구성을 요구하지 않는다.


---

## Gradle  

- 자바에서 maven과 함께 가장 많이 사용되는 빌드 Tool이다
- 개발자가 작성한 코드 컴파일 ,배포를 위한 추가적인 작업을 수행 하며 패키징을 수행한다.
- 필요한 라이브러리를 (의존성 관리) 한다.
- 빌드도구가 없다면 개발자가 직접 라이브러리를 다운받고 경로를 설정하고 패키징 해야한다.
- 멀티모듈 프로젝트관리 , 병렬 building이 가능하다.
- gradleDSL을 통해 gradle이 기본제공하는 task와 plugin을 개발자가 직접 커스터마이징 할 수 있다.

### gradle이 제공하는 기능

- `build (빌드)` : 컴피일 테스트 검사 배포 등의 과정을 포함한다.
- `compile (코드 컴파일)` : 테스트를 포함한 소스코드를 컴파일 한다.
- `Packaging(패키징)` : 배포할 수 있는 Jar ,War 형태로 파일을 묶는다
- `File Handling (패키징)` : 파일과 디렉토리를 생성 , 복사 ,삭제하는 기능을 지원
- `Test Automation (테스트 자동화)`
- `Generate API Docs (문서 생성 지원)`
- `Code Quality Analysis(코드 품질 분석)`
- `Deploy(배포)` : 설정에 따라 넥서스 등 원격저장소에 배포하는 과정을 수행한다.
- `Dependency management(의존성관리)`


### Gradle의 Dependency management(의존성관리)

- MavenCentralRepositroy 에서 라이브러리를 내려받을 수 있으며 
- 내려받은 라이브러리를 gradle이 프로그램에서 사용할 수 있게 관리해준다.
- io.spring.dependency-management를 통해  Bom(bill of Materials)기능을 사용하여 라이브러리 버전을 관리한다.
- 프로젝트 스프링 부트 모듈의 버전에 따라 그에 맞는 라이브러리들의 버전이 정의되어 있기 때문에
- 라이브러리의 버전을 따로 적지 않아도 알아서 버전을 맞춘다.
- Bom에 정의된 라이브러리 버전보다 낮은 버전의 라이브러리를 사용할 경우 호환성에 문제가 있을 수 있다
- 아래의 세가지에서 스프링 부트 버전에 따른 라이브러리 버전 정보들을 확인 할 수 있다.
  * `Spring boot Release Note` 
  * `Spring boot Reference Document - appendix F: Dependency Versions` 
  * `Github: spring-boot-dependencies/build.gradle </br>`

 ** `BOM(Bill of Materials)`: 한 곳에서 연관된 라이브러리 버전을 기록하고 이를 읽어서
프로젝트에서 사용하는 라이브러리 버전을 관리하는 메이븐의 기능 

#### maven repository 라이브러리 검색사이트

- 아래 사이트에서 메이븐 혹은 그래이들의 디펜던시 스크립트를  확인할 수 있다.
- https://search.maven.org/
- https://mvnrepository.com/

---

## Spring boot application  살펴보기



```java
//스프링 부트 탐색지점 & 자동구성 활성화를 선언
@SpringBootApplication
public class UgoApplication{
    //메인 클래스 진입점
    public static void main(String [] args){
        //스프링부트 애플리케이션 구동지점
        SpringApplication.run(UgoApplication.class , args);
    }
}
```

### SpringBootApplication class 
- SpringApplication 클래스의 run 를 타고 들어가면 아래와 같은 코드가 정의되어있다.

````java

public ConfigurableApplicationContext run(String... args) {
      long startTime = System.nanoTime();
      DefaultBootstrapContext bootstrapContext = createBootstrapContext();
      ConfigurableApplicationContext context = null;
      configureHeadlessProperty();
      SpringApplicationRunListeners listeners = getRunListeners(args);
      listeners.starting(bootstrapContext, this.mainApplicationClass);
      try {
          ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
          ConfigurableEnvironment environment = prepareEnvironment(listeners, bootstrapContext, applicationArguments);
          configureIgnoreBeanInfo(environment);
          Banner printedBanner = printBanner(environment);
          //설정 정보를 읽어 들이고 애플리케이션컨텍스트를 생성한다
          context = createApplicationContext();
          context.setApplicationStartup(this.applicationStartup);
          prepareContext(bootstrapContext, context, environment, listeners, applicationArguments, printedBanner);
          refreshContext(context);
          afterRefresh(context, applicationArguments);
          Duration timeTakenToStartup = Duration.ofNanos(System.nanoTime() - startTime);
          if (this.logStartupInfo) {
              new StartupInfoLogger(this.mainApplicationClass).logStarted(getApplicationLog(), timeTakenToStartup);
          }
          listeners.started(context, timeTakenToStartup);
          callRunners(context, applicationArguments);
      }
      catch (Throwable ex) {
          handleRunFailure(context, ex, listeners);
          throw new IllegalStateException(ex);
      }
      try {
          Duration timeTakenToReady = Duration.ofNanos(System.nanoTime() - startTime);
          listeners.ready(context, timeTakenToReady);
      }
      catch (Throwable ex) {
          handleRunFailure(context, ex, null);
          throw new IllegalStateException(ex);
      }
      return context;
  }
    
````

- applicationContextFactory에게 애플리케이션 컨텍스트 생성을 요청한다   

```` java
protected ConfigurableApplicationContext createApplicationContext() {
		return this.applicationContextFactory.create(this.webApplicationType);
}

/**
  - applicationContextFactory.create(this.webApplicationType);
  - ApplicationContextFactory는 해당 애플리케이션 타입에따라 Servlet , reactvie 두가지 구현체가 있다 
  - 아래는 Servlet타입 애플리케이션을 위한 AnnotationConfigServletWebServerApplicationContext에서
  - 컨텍스트를 생성하는 코드이다.
*/
static class Factory implements ApplicationContextFactory {
    @Override
    public ConfigurableApplicationContext create(WebApplicationType webApplicationType) {
        return (webApplicationType != WebApplicationType.SERVLET) ? null
                : new AnnotationConfigServletWebServerApplicationContext();
    }

}
````

### @SpringBootApplication

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
// 스프링부트 설정정보(@Configuration) 클래스임을 선언한다.
@SpringBootConfiguration
// AutoConfigure 구성 정보를 읽어 활성화 시킨다.
@EnableAutoConfiguration
// @SpringBootApplication의 위치를 기준으로 컴포넌트를 탐색하고 빈으로 등록한다.
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {

```


---

## 스프링 부트 자동구성 (AutoConfiguration)

- 스프링부트 AutoConfiguration 순서
```java
@SpringBootApplication
 -> @EnableAutoConfiguration
    -> @Import(AutoConfigurationImportSelector.class)
        -> AutoConfigurationImportSelecter.class
            -> SpringFactoriesLoader
                -> META-INF/spring.factories
```









