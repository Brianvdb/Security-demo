# Security 2 Encryptie applicatie

## Gebruikte libraries
* Spark Framework: een micro web application framework voor Java 8
* Bouncy Castle: een encryptie library met handige base64 utilities
* MongoDB driver: voor het connecten met een database binnen Java
* Gson: voor het omzetten van JSON naar Model klassen.
* Maven: package manager
* JQuery: voor het doen van AJAX calls

## Gebruikte technieken
* Symmetrische encryptie
  * Random salt m.b.v. SecureRandom
  * AES (AES/CBC/PKCS5Padding)
  * PBKDF2-HMAC-SHA256 voor het aanmaken van een secret key met een salt en password
* RESTFUL Service

## Informatie
De encryptie/decrypte gebeurd in de SuperSecretMaker klasse.