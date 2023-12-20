# Clean Architecture Java-Spring

An implementation of the clean architecture architectural pattern.

The goal of this service is to manage scores from different kind of simple video games,
and make available a simple leaderboard for these scores.

Video game services can push their score to the leaderboard through a secure API, which in the
future will be protected by Oauth2 and Keycloak.


Following the  [ports and adapter architecture](https://codesoapbox.dev/ports-adapters-aka-hexagonal-architecture-explained/), we have three repositories :
- **core** (or domain) for the domain code. Independant of frameworks, databases, etc.
- **details** (or infrastructure), for the implementation of driven ports (mainly data storage)
- **app**, for the framework (spring boot here) 

We also have an **apiTests** module for making E2E test at the network request level.

Inspiration taken from [this talk by steve pember](https://www.youtube.com/watch?v=mbNzUkNjrnA) and [the following](https://github.com/spember/spring-shoestore) github repo.
The following additions have been made :
- Code in java instead of kotlin
- Use of gradle modules
- Use of Java records