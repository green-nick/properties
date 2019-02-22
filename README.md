[![](https://jitpack.io/v/green-nick/properties.svg)](https://jitpack.io/#green-nick/properties)
# Properties
Small, lightweight library that allows to use observable properties.  
Written in pure Kotlin, without third-party dependencies, Kotlin oriented.  
The difference from Kotlin's delegate `observable` is that you able to add listeners at any moment instead of only in initialization.

Could be useful in MVVM patterns, when you need to bind `Views` and `ViewModels`.

## How to add to Project:
**Step 1.** Add the JitPack repository to your build file.  
Add this in your module's build.gradle at the end of repositories:  
```
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```
**Step 2.** Add the dependency
```
dependencies {
    implementation 'com.github.green-nick:properties:{latest version}'
}
```
## Usage examples:
You can find usage examples in the unit-tests [package](https://github.com/green-nick/properties/tree/master/src/test/java/com/github/greennick/properties)
