# CatForemanApp2

JobSiteOverviewActivity.java ~ line #190
Understanding Signal R HubProxy.on() method.

First Parameter is the method name on the Signal R end of the application.

Second parameter is a Subscription Handler. This can be SubscriptionHandler1...SubscriptionHandler5. The number at the end signifies how
many parameters will be included in the run method. Make sure to list these objects generically in the <> after SubscriptionHandlerX. 
The parameters are objects, not primitives (so use Float not float).

After the SubscriptionHandler, list out each Object.class

Example of a SubscriptionHandler1
```
hub.on("signalRMethod1",
    SubscriptionHandler1<Float>(){
        @override
        public void run(Float number){
            code to run
        }
    }, Float.class);
  ```
  
Example of a SubscriptionHandler2
```
hub.on("signalRMethod2",
    SubscriptionHandler1<Float, Int>(){
        @override
        public void run(Float number, Int count){
            code to run
        }
    }, Float.class, Int.class);
  ```

And so on and so forth up to SubscriptionHandler5.
