# api-voting
API system for orchestrating votes in an poll

Project developed with
- [Java 11] (https://www.oracle.com/br/java/technologies/javase-jdk11-downloads.html)
- [Maven 3.5.3] (http://mirror.nbtelecom.com.br/apache/maven/maven-3/3.5.3/binaries/apache-maven-3.5.3-bin.zip)
- [IntelliJ IDEA] (https://www.jetbrains.com/idea/)
- [Flyway] (https://flywaydb.org/documentation/)
- [MySQL] (https://dev.mysql.com/downloads/mysql/)

## Development

```shell
cd "directory for the repository"
git clone https://github.com/dalyleide/api-voting.git
```
Downloads api-voting repository to your local computer

## Build

```shell
mvn flyway:migrate
```
The command will download all project dependencies and create a * target * directory with the built artifacts, which include the project jar file. In addition, unit tests will be performed, and if any fail, Maven will display this information on the console.

```shell
mvn clean install
```
Migrates the schema to the latest version. Flyway will create the schema history table automatically if it doesn’t exist.
And creates the tables used by the application

## License
 
**GNU General Public License** , **GNU GPL**, **GPL**.
 
[GPLv3](https://www.gnu.org/licenses/gpl-3.0.html) 