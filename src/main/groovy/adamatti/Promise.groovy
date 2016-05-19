package adamatti

import groovy.util.logging.Slf4j

import java.util.concurrent.LinkedBlockingQueue

@Slf4j
class Promise {
    private boolean completed = false
    final Queue<Closure> queue = new LinkedBlockingQueue<Closure>()
    Closure errorHandler = {error -> log.error("Error: ", error)}
    Closure completeHandler = {->}

    public boolean isCompleted (){completed}
    public boolean isPending() {!completed}

    public Promise(){

    }
    public Promise(Closure action){
        queue.add(action)
    }

    public Promise go(){
        runAsync(queue.poll())
        this
    }

    public Promise onError(Closure errorHandler){
        this.errorHandler = errorHandler
        this
    }
    public Promise then(Closure action){
        queue.add(action)
        this
    }

    public Promise onComplete(Closure completeHandler){
        this.completeHandler = completeHandler
    }

    private runAsync(Closure action, args = null){
        try {
            new Thread() {
                public void run() {
                    this.runSync(action,args)
                }
            }.start()
        } catch (Exception e){
            errorHandler.call(e)
            this.doComplete()
        }
    }

    private runSync(Closure action, args = null){
        try {
            def result = args ? action.call(args) : action.call()
            if (!queue.isEmpty()) {
                runAsync(queue.poll(), result)
            } else {
                this.doComplete()
            }
        } catch (Exception e){
            errorHandler.call(e)
            this.doComplete()
        }
    }

    private void doComplete(){
        completed = true
        completeHandler.call()
    }
}
