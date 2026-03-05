Feature: re-usable feature to create a session (for a game)

  # Will be run before each scenario to set up variables
  Background:
    * url baseUrl
    * def scoreSecure = '/secure/score'

  Scenario: Create a session
    Given url baseUrl
    And path sessionSecure
    And request { }
    And header Accept = 'application/json'
    When method post
    Then status 201
    And match response == { sessionId: '#string' }
    * def sessionId = response['sessionId']
    * print response
    And print 'The value of sessionId is ?? :', sessionId