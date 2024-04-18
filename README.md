Hi all!
This a console script program that parses the list of JSON files of the main entity and generates statistics (total number) in terms of one of its attributes. 

How to run the program: 
1) go to the root of the project directory, open cmd in it;
2) write in cmd - "mvn clean pakage", for correct reassembly or assembly of the project;
3) next step writie this - "mvn exec:java -Dexec.mainClass="ua.assignmentOne.XmlStatisticsGenerator" -Dexec.args="args1 args2". Where args1 - Your path to the JSON files from which statistics will be generated, args2 - the value for which statistics will be collected, for example - "author"./

The project also contains prepared JSON files, "./srs/main/resources/".
