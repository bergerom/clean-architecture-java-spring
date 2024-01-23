# TODO : use callOnce feature to propagate the value of session ID across scenarios
# See : https://github.com/karatelabs/karate/blob/master/karate-demo/src/test/java/demo/callonce/call-once.feature
Feature: Create user, add score, and show score board

  Background:
    * url baseUrl
    * def users = '/users/'
    * def scores = '/scores/'
    * def scoreSecure = '/secure/score'
    * def sessionSecure = '/secure/session'
    * def uuid = function(){ return java.util.UUID.randomUUID() + '' }
    * def randomUuid = uuid()

  Scenario: Create an user

    Given path users
    And request { userName: 'John', age: 30 }
    And header Accept = 'application/json'
    When method post
    Then status 200
    And match response == { userId: '#string' }
    And print 'The value of userId is :', response['userId']

  Scenario: Create a session

    Given path sessionSecure
    And request { }
    And header Accept = 'application/json'
    When method post
    Then status 201
    And match response == { sessionId: '#string' }
    * def sessionId = response['sessionId']
    And print 'The value of sessionId is :', sessionId

  Scenario: Add a score

    Given path scoreSecure
    And print 'Trololo The value of sessionId is :', sessionId
    And request { gameSessionId: '#(sessionId)', score: 5, date: "2024-01-23T11:00:27.907667238Z"}
    And header Accept = 'application/json'
    When method post
    Then status 201
    And match response == { sessionId: '#string' }
    And print 'The value of sessionId is :', response['sessionId']

  Scenario: Get all score

    Given path scores
    And request {}
    And header Accept = 'application/json'
    When method get
    Then status 200
    And match response == { userId: '#string', totalScore: '#number'}
    And print 'The score board is', response