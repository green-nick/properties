[![](https://jitpack.io/v/green-nick/properties.svg)](https://jitpack.io/#green-nick/properties)
[![](https://jitci.com/gh/green-nick/properties/svg)](https://jitci.com/gh/green-nick/properties)

# Properties
Small, lightweight library that allows to use observable properties.  
Written in pure Kotlin, without third-party dependencies, Kotlin oriented.  
The difference from Kotlin's delegate `observable` is that you able to add listeners at any moment instead of only in initialization.

Could be useful in MVVM patterns, when you need to bind `Views` and `ViewModels`.

Usage in real project: [Karbon](https://gitlab.com/greennick/karbon)

## Usage examples:
### Initialization:
Create non-null mutable property with default value:

`val property: MutableProperty<String> = propertyOf("Hello!")`

### Assignment & Reading:
Assignment new value to the property (also will update active observers):

`property.value = "this string will be assigned and pushed to all observers"`

Notice, that you able to change value of mutable property only!
```
val mutable: MutableProperty<String> = propertyOf("Hello!")
mutable.value = "world!" // this works

val immutable: Property<String> = mutable
immutable.value = "or not" // but this doesn't
```

Reading current value from the property:
```
val property = propertyOf("Hello")
val currentValue = property.value // currentValue will get "Hello"
```
### Listening to changes:
After subscribing on property, you will receive current value immediately:
```
val property = propertyOf("Hello")

property.subscribe {
    println("receive [$it]")
}

property.value = "world!"
```
Output:

```
receive [Hello]
receive [world!]
```

Also you able to handle lifecycle of subscriptions and unsubscribe when you don't want receive any updates anymore:
```
val property = propertyOf("Hello")

val subscription: Subscription = property.subscribe {
    println("receive [$it]")
}

property.value = "world!"

subscription.unsubscribe()

property.value = "or not" // this update won't be printed into console
```
Output:

```
receive [Hello]
receive [world!]
```
### Types:
#### `propertyOf`
Default one. Allows to read and assign values and listen its changes.
Does equality check when new value is being assigned.
So if new value is the same as previous one - change listener won't be triggered.
#### `emptyProperty`
Just another initializer of `propertyOf`.
Allows not to set init value and final type will be nullable anyway.
#### `triggerPropertyOf`
Unlike `propertyOf`, this property doesn't use equality check at all.
This means that it will be triggered on every new assignment even if new value the same as previous one.
#### `firePropertyOf`
Special property that emits value only one time. 
If there is new subscription, it won't receive updates until new assignment will be done.

**Pay attention**, that there is **only one active subscriber exist**.
Every new subscription will cancel previous one automatically.
#### `debouncePropertyOf`
Only set an item to Property if a particular delay has passed without it setting another item.
Has a version that uses Java's `SingleThreadScheduledExecutor`, but you can set your own.
Also preferable to use `CoroutineScope`s extended `debounceProperty` which schedule updates 
on specified scope and context.

### Extensions:
#### General:

You can use invoke call for subscribing instead of `subscribe`:
```
val property = propertyOf("Hello")

property.invoke {
    println("receive [$it]")
}
```
or simplified form:
```
property {
    println("receive [$it]")
}
```
There is also extensions for nullable properties:
```
val property = propertyOf<String?>("hello")

property.subscribeNonNull { value: String ->
    println(value)
}

property.reset() // set null value
```
#### Mapping:
You also able to map one property to another:
```
val property = propertyOf("Hello")

val length: MutableProperty<Int> = property.map { it.length } // will contain 5
```
Also notice that mapped value will be triggered on all updates of origin one.

Besides that, there is mapper for nullable properties:
```
val origin = propertyOf<String?>(null)

val length: MutableProperty<Int> = property.mapNotNull(0) { it.length } // will contain 0

origin.value = "hello"
length.value == 5
```
If init value of origin property is `null`, default value will be used for initialization.  
All non-null set values will be mapped.
#### Filtering:
You can filter values from one Property to another:
```
val property = propertyOf("Hello")

val filtered: MutableProperty<String?> = property.filter { it.length <= 5 }
filtered.value == "Hello"

property.value = "world"
filtered.value == "world"

property.value = "eleven"
filtered.value == "world"
```
Also notice that filtered Property will be triggered on all suitable updates of origin one.

By default, `.filter` produces nullable Properties. To prevent that, you can set default value, 
which will be used in case, if current value of origin Property didn't pass the filter :
```
val property = propertyOf("Hello")

val filtered: MutableProperty<String> = property.filter("verylongword") { it.length >= 10 } // will contain "verylongword"

origin.value = "anotherverylongword"
filtered.value == "anotherverylongword"
```
#### Addition:
You can add two different properties and get new one and receive all updates pushed to origins as Pair of their values:
```
val hi = propertyOf("Hello")
val person = propertyOf("world")

val greeting: Property<Pair<String, String>> = hi + person

greeting.subscribe { (hi, person) ->
    println("$hi, $person!") // prints "Hello, world!"
}
hi.value = "Aloha" // prints "Aloha, world!"
person.value = "Github" // prints "Aloha, Github!"
```
#### Zipping:
Similar to Addition, but allows you to convert output into single object instead of Pair:
```
val hi = propertyOf("Hello")
val person = propertyOf("world")

val greeting: Property<String> = hi.zipWith(person) { hi, person ->
    "$hi, $person!"
}

greeting.subscribe {
    println(it) // prints "Hello, world!"
}
hi.value = "Aloha" // prints "Aloha, world!"
person.value = "Github" // prints "Aloha, Github!"
```
#### Booleans:
There are few extension for Properties that holds boolean value.

First one is `toggle`. It allows to invert value stored in property. 
Could be called only on `MutableProperty<Boolean>`
```
val property: MutableProperty<Boolean> = propertyOf(true)
val currentValue = property.value // true

property.toggle()
val newValue = property.value // false
```

Second one is logical `not` operator. It creates new property with inverted value of parent's:
```
val property: MutableProperty<Boolean> = propertyOf(true)
val inverted = !property

println(inverted.value) // false

property.value = false
println(inverted.value) // true
```

Beside that, there are two additional extensions: `subscribeOnTrue` and `subscribeOnFalse`.

Callback will be triggered only if correspond value will be set into property:
```
val property = propertyOf(true)

property.subscribeOnTrue { println("received true") }
property.subscribeOnFalse { println("received false") }

property.value = false
property.value = true
```
Output:
```
received true
received false
received true
```
### Memoization:
You able to keep all changes set to property by using `.memoized` extension.  
It creates wrapper around given property which save all changes and allow to navigate through them:
```
val prop = propertyOf("hello").memoized
prop.size // 1
prop.position // 0
prop.history // listOf("hello")

prop.value = "world"
prop.size // 2
prop.position // 1
prop.history // listOf("hello", "world")

prop.position = 0 // navigation through history
prop.value == "hello"
```
There are two extensions for memoized property:
```
val prop = propertyOf("hello").memoized

prop.first() // moves position to 0 (init value)
prop.last() // moves position to latest set value, which is (size - 1)
```
If you need you can combine few memoized properties into one unit - `CompositeMemoize`  
It allows to navigate through all changes done in all inner properties.
```
val name = propertyOf("John").memoize
val age = propertyOf(20).memoize
val type = propertyOf(User.FREE).memoize

val history = CompositeMemoize(name, age, type)

name.value = "Paul"
name.value = "Peter"
age.value = 21
age.value = 25
type.value = User.PAID
 
// history.position = 0 -> name = "John"; age = 20; type = FREE;
// history.position = 2 -> name = "Peter"; age = 20; type = FREE;
// history.position = 5 -> name = "Peter"; age = 25; type = PAID;
// and etc.
```

Changes of `history`:

value/position |  0  |  1  |  2  |  3  |  4  |  5
-------------|-----|-----|-----|-----|-----|---
name: |John | Paul | Peter
age: | 20 | | | 21 | 25
type: | FREE | | | | |PAID

Also you can find additional usage examples in the unit-tests [package](https://github.com/green-nick/properties/tree/master/src/test/java/com/github/greennick/properties)

## How to add to your project:
**Step 1.** Add the JitPack repository to your build file.  
Add this in your module's build.gradle at the end of repositories:  
```
repositories {
    maven { url 'https://jitpack.io' }
}
```
**Step 2.** Add the dependency
```
dependencies {
    implementation "com.github.green-nick:properties:{put latest version here}"
}
```
