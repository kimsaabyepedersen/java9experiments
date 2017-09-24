# Java 9 Experiments

## Modules
The project consists of two modules:
* `provider` - it has an interface in `api.MyApi` with some methods. It also has an implementation in its `implementation` package.
* `consumer` - it wants to use an implementation of the interface `api.MyApi` from the provider. It also has a `main` method that you will need to execute when fiddling about with the `module-info.java` files. 


The project itself happens to be a Maven Multi module project.

### Things to notice in the provider module
The two classes in the helper package are `public`. Also the implementation of the `api.MyApi` found in the `implementation` package is `public`.

The `module-info.java` file:
* exports the `api` package - if removed the `api.MyApi` class would be hidden to other modules meaning other modules would not compile.
* provides the `api.MyApi` with the class `implementation.MyApiImpl` - if removed other the implementing class MyApiImpl would not be "available" to other modules meaning the `MyApi.class` would fail to load.

I write "available" in quotes because though the MyApi can now be resolved by other modules using the `ServiceLoader` but doing a new `new MyApiImpl()` in the consumer wont work. So the implementation of the interface is hidden from consumers - though it is public in the module where it is declared.

#### What happens if I remove the `exports api` in the `module-info.java`?
The provider compiles file but the consumer fails to compile with these errors:

This relates to the `module-info.java` in the consumer module. 
```
[ERROR] <PATH>/module-info.java:[3,10] package api is not visible
[ERROR] (package api is declared in module provider, which does not export it)
```
And an error in all files that use the api package (in my case `MyService`:)
```
[ERROR] <PATH>/MyService.java:[5,8] package api is not visible
[ERROR] (package api is declared in module provider, which does not export it)
```

#### What happens if I remove the `provides api.MyApi with implementation.MyApiImpl;` in the `module-info.java`?
All things compile, but the `ServiceLoader` in `MyService` in the `consumer` module will not be able to load `MyApi.class` as no modules provide an implementation service. This will be discovered at runtime when the consumer tries to load the class.

### Things to notice in the consumer module
The `module-info.java` file:
* requires the `provider` module.
* uses the `api.MyApi`.

#### What happens if I remove the `requires provider` in the `module-info.java`?
When I recompile I'll be met with one error relating to `module-info.java` file:
```
[ERROR] <PATH>/module-info.java:[2,10] package api is not visible
[ERROR] (package api is declared in module provider, but module consumer does not read it)
```
And an error in all files that use the api package (in my case `MyService`:)
```
[ERROR] <PATH>/MyService.java:[5,8] package api is not visible
[ERROR] (package api is declared in module provider, but module consumer does not read it)
```

#### What happens if I remove the `uses api.MyApi` in the `module-info.java`?
Now the module compiles, hurray. But I'll be met by this runtime exception:
```
Exception in thread "main" java.util.ServiceConfigurationError: api.MyApi: module consumer does not declare `uses`
	at java.base/java.util.ServiceLoader.fail(ServiceLoader.java:588)
	at java.base/java.util.ServiceLoader.checkCaller(ServiceLoader.java:574)
	at java.base/java.util.ServiceLoader.<init>(ServiceLoader.java:503)
	at java.base/java.util.ServiceLoader.load(ServiceLoader.java:1684)
	at consumer/service.MyService.main(MyService.java:11)
```

### Prerequisites
Obviously you need Java 9. You also new a newer version of Maven. I use 3.5.0.

