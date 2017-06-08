package io.kaz.app

import java.math.BigInteger

enum class Transformation { ID, DIV2, MULT3PLUS1 }

/**
 * Created by kasra on 6/6/17.
 */
fun main(args: Array<String>) {
  // for each number in the range
  //   get the value in terms of BigInteger
  //   while the value is not 1
  //     show the value
  //     get the next value: value = collatz(value)

  println(collapze(3))
}

//TODO collapze for ranges
fun collapze(a: Int): List<BigInteger> {
  return collapze(a.b)
}


fun collapze(a: BigInteger): List<BigInteger> {
  val result = ArrayList<BigInteger>()

  var value = a
  result += value
  while (value != 1.b) {
    value = collatz(value)
    result += value
  }

  return result
}

fun collatz(a : BigInteger): BigInteger {
  if (a == 1.b) {
    return a
  }

  if (a.mod(2) == 0.b) {
    return a / 2
  } else {
    return a*3 + 1
  }
}

fun show(value: BigInteger) {
  print("%-5d".format(value))
}

//region --- BigInteger operators ---

private val Int.b: BigInteger
  get() = BigInteger.valueOf(this.toLong())

operator fun BigInteger.times(other: Int): BigInteger {
  return this * other.b
}

operator fun BigInteger.plus(other: Int): BigInteger {
  return this + other.b
}

operator fun BigInteger.div(other: Int): BigInteger {
  return this / other.b
}

operator fun BigInteger.mod(other: Int): BigInteger {
  return this % other.b
}

//endregion