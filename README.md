# Score Board Library

This library is implemented as a Spring Boot starter and is used for demo purposes only. In-memory store solution is
implemented only.

### Technologies:

* Java 17
* Spring Boot 3
* Jakarta Validation
* JUnit 5
* AssertJ

### Usage

#### Building project
First of all you should run maven build command from the root directory of the project:

```sh
./mvnw clean install
 ``` 
You need to add library to your project classpath. For maven example:

```xml

<dependency>
    <groupId>com.sportradar</groupId>
    <artifactId>score-board</artifactId>
    <version>0.0.1</version>
</dependency>
```

Currently, only internal storage implementation is supported. Thus, no additional configuration is required. Just add dependency in your code:
```java
@Autowired
private ScoreBoard scoreBoard;
```
### Specification
```java
public interface ScoreBoard {

    /**
     * Creates match with score 0-0 for provided <CODE><I>matchDetails</I></CODE>
     *
     * @param matchDetails contains home and away team names {@link MatchDetails}
     * @throws ConstraintViolationException if <CODE><I>matchDetails</I></CODE> is invalid
     * @throws com.sportradar.core.scoreboard.exception.MatchAlreadyStartedException
     * if match for particular teams has been started already
     */
    void startNewScore(@Valid MatchDetails matchDetails);

    /**
     * Updates score for specific match
     *
     * @param matchScore contains home and away team names and scores {@link MatchScore}
     * @throws ConstraintViolationException if <CODE><I>matchScore</I></CODE> is invalid
     * @throws com.sportradar.core.scoreboard.exception.MatchNotFoundException
     * if match for particular teams doesn't exist
     */
    void updateScore(@Valid MatchScore matchScore);

    /**
     * Removes match from score board
     *
     * @param matchDetails contains home and away team names {@link MatchDetails}
     * @throws ConstraintViolationException if <CODE><I>matchDetails</I></CODE> is invalid
     * @throws com.sportradar.core.scoreboard.exception.MatchNotFoundException
     * if match for particular teams doesn't exist
     */
    void finishScore(@Valid MatchDetails matchDetails);

    /**
     * Returns list of matches ordered by score and creation time in descending order
     *
     * @return ordered list of {@link MatchScore match scores}
     */
    List<MatchScore> listScores();

    /**
     * Cleans up score board
     */
    void cleanUp();
}
```


