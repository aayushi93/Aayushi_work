import scala.reflect.io.File

/**
 * Count number of elements
 * Get the first element
 * Get the last element
 * Get the first 5 elements
 * Get the last 5 elements
 */
val ls = List.range(0,10)
println("First element: " + ls.head)
println("Last element: " + ls.last)
println("First 5 elements: " + ls.take(5))
println("Last 5 elements: " + ls.takeRight(5))


/**
 * Double each number from the numList and return a flatten list
 * e.g.res4: List[Int] = List(2, 3, 4)
 *
 * Compare flatMap VS ls.map().flatten
 */
val numList = List(List(1, 2), List(3))
println("Doubled number: " +numList.flatMap(i => i.map(_ * 2)))


/**
 * Sum List.range(1,11) in three ways
 * hint: sum, reduce, foldLeft
 *
 * Compare reduce and foldLeft
 */
val sumNum = List.range(1,11)
println("Using 'Sum': " + sumNum.sum)
println("Using 'foldLeft': " + sumNum.foldLeft(0)((x:Int, y:Int) => x + y))
println("Using 'reduce': " + sumNum.reduceLeft((x, y) => x+ y))


/** Map question1:
* Compare get vs getOrElse (Scala Optional)
* countryMap.get("Amy");
* countryMap.getOrElse("Frank", "n/a");
*/
val countryMap = Map("Amy" -> "Canada", "Sam" -> "US", "Bob" -> "Canada")
println("Using get: " + countryMap.get("Amy"))
println("Using get: " + countryMap.get("May"))
println("Using getOrElse: " + countryMap.getOrElse("Bob", "n/a"))
println("Using getOrElse: " + countryMap.getOrElse("Aayushi", "n/a"))
/** 'country.get' outputs the optional value of the key if present
 * in the map. If the key is not present, it returns None.
 * If we want to avoid Some(value) in the output,we can fetch
 * the value directly. But if the key is not present,
 * NoSuchElementException will be thrown.
 * 'country.getOrElse' returns the value of the key else it will
 * return the default value mentioned.
 */


/**
 * Map question2:
 * create a list of (name, country) tuples using `countryMap` and `names`
 */
val names = List("Amy", "Sam", "Eric", "Amy")
val country = names.map(s => (s, countryMap.getOrElse(s, "n/a")))


/**
 * Map question3:
 * count number of people by country. Use `n/a` if the name
 * is not in the countryMap  using `countryMap` and `names`
 * e.g. res0: scala.collection.immutable.Map[String,Int] =
 * Map(Canada -> 2, n/a -> 1, US -> 1)
 */


def peopleNum(peopleMap: Map[String, Int], string: String) : Map[String, Int] =
{
  val country = countryMap.getOrElse(string, "n/a")
  peopleMap + (country -> (peopleMap.getOrElse(country, 0) + 1))
}
println(names.foldLeft(Map.empty[String, Int])(peopleNum))

/**
 * number each name in the list from 1 to n
 * e.g. res3: List[(Int, String)] = List((1,Amy), (2,Bob), (3,Chris))
 */
val names2 = List("Amy", "Bob", "Chris", "Dann")
val nameList = List.range(1,names2.length).zip(names2)
println(nameList)


/**
 * SQL questions 1:
 *
 * read file lines into a list
 * lines: List[String] = List(id,name,city, 1,amy,toronto, 2,bob,calgary, 3,chris,toronto, 4,dann,montreal)
 */
import scala.io.Source
val fileName = "/home/centos/dev/jarvis_data_eng_Aayushi/spark/src/main/resources/employees.csv"
val sourceFile = Source.fromFile(fileName)
val fileLines = sourceFile.getLines().toList
sourceFile.close()


/**
 * SQL questions 2:
 *
 * Convert lines to a list of employees
 * e.g. employees: List[Employee] = List(Employee(1,amy,toronto), Employee(2,bob,calgary), Employee(3,chris,toronto), Employee(4,dann,montreal))
 */
case class Employee(id:String, name:String, city:String, age:String)
var empMap = (line:String) => {
  val sep = line.split(",")
  Employee(sep(0), sep(1), sep(2), sep(3))

}

val noHeader = fileLines.drop(1)
val newList = noHeader.map(empMap)


/**
 * SQL questions 3:
 *
 * Implement the following SQL logic using functional programming
 * SELECT uppercase(city)
 * FROM employees
 *
 * result:
 * upperCity: List[Employee] = List(Employee(1,amy,TORONTO,20), Employee(2,bob,CALGARY,19), Employee(3,chris,TORONTO,20), Employee(4,dann,MONTREAL,21), Employee(5,eric,TORONTO,22))
 */
val upperCity = newList.map(emp => new Employee(emp.id, emp.name, emp.age, emp.city.toUpperCase))


/**
 * SQL questions 5:
 * Implement the following SQL logic using functional programming
 * SELECT uppercase(city), count(*)
 * FROM employees
 * GROUP BY city
 */
def cityNum(cityMap:Map[String, Int], cityName:String) : Map[String, Int] = {
  cityMap + (cityName -> (cityMap.getOrElse(cityName, 0) + 1))
}
newList.map(empMap => empMap.city.toUpperCase).foldLeft(Map.empty[String, Int])(cityNum)
println(newList.map(empMap => empMap.city.toUpperCase).groupBy(identity).mapValues(_.size))


/**
 * SQL question 6:
 * Implement the following SQL logic using functional programming
 * SELECT uppercase(city), count(*)
 * FROM employees
 * GROUP BY city,age
 */
def cityFunc(cityMap:Map[(String, Int), Int], emp:Employee) : Map[(String, Int), Int] = {
  val cityAge = (emp.city.toUpperCase, emp.age.toInt)
   cityMap + (cityAge -> (cityMap.getOrElse(cityAge, 0) + 1 ))
}

val res =   newList.foldLeft(Map.empty[(String, Int), Int])(cityFunc)
newList.groupBy(x => (x.city.toUpperCase, x.age)).mapValues(_.size)
