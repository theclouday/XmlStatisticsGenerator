Hi all!
This a console script program that parses the list of JSON files of the main entity and generates statistics (total number) in terms of one of its attributes. 

**How to run the program:** 
1) go to the root of the project directory, open cmd in it;
2) write in cmd - "mvn clean pakage", for correct reassembly or assembly of the project;
3) next step writie this - "mvn exec:java -Dexec.mainClass="ua.assignmentOne.XmlStatisticsGenerator" -Dexec.args="args1 args2". Where args1 - Your path to the JSON files from which statistics will be generated, args2 - the value for which statistics will be collected, for example - "author".

The project also contains prepared JSON files, "./srs/main/resources/".
An example of such a file: 
[{
    "title": "Brave New World",
    "author": "Charlotte Bronte",
    "year_published": 1804,
    "genre": "Thriller"
}
, {
    "title": "Brave New World",
    "author": "D.H. Lawrence",
    "year_published": 1754,
    "genre": "Romance, Thriller"
}
]. 

If we set "genre" as the second argument, we get these statistics:
<statistics>
  <item>
    <value>Thriller</value>
    <count>2</count>
  </item>
  <item>
    <value>Romance</value>
    <count>1</count>
  </item>

The **main** entities will be:
· Book,
· Author.


I did some **tests on using different numbers of threads** when reading files. And here's what the results showed:
1.78 ms 10 files - 2 threads
1.55 ms 12 files - 4 threads
1.36 ms 14 files - 6 threads
1.65 ms 18 files - 6 threads
1.70 ms 25 files - 12 threads
