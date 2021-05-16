package com.gpfei.recruit.kotlin.kotlin


infix fun String.beginWith(prefix: String) = startsWith(prefix)

infix fun String.equal(prefix: String) = equals(prefix)

infix fun String.unEqual(prefix: String) = !equals(prefix)

infix fun String.endWith(prefix: String) = endsWith(prefix)

infix fun String.contain(prefix: String) = contains(prefix)

