//Output:  Greetings!
println("Greetings!")

//Output: 22
var x: Int = 22
println(x)

//Output: 99
var y: Int = 99
println(y)

//Output: pretty
var aa: String = "hello"
aa = "pretty"
println(aa)

////Output: Second line gives error as value of bb can be
//// replaced by an Integer only. println(bb) prints 10.
//var bb: Int = 10
//bb = "funny"
//print(bb)

//Output: Prints 3.14
val cc: Double = 3.14
println(cc)

////Output: Error
//val dd: Double = 9.99
//dd = 10.01
//println(dd)

//Output: Type of gg is Double. If the type is not explicitly
//mentioned then Scala is smart enough to guess the type.
var gg = 8.88

//Scala Conditionals
if(5>1) {
  println("5 is greater than 1")
}