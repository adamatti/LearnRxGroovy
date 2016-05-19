package adamatti

import groovy.util.logging.Slf4j

/**
 * Quick tests using a self made Promise
 */
@Slf4j
class Sample2 {
    public static void main(String [] args){
        Promise p = new Promise({ ->
            log.debug "Promise 1 - start"
            sleep(1000)
            log.debug "Promise 1 - end"
            return ["a","b"]
        }).then{ result ->
            log.debug "Promise 2 - start - ${result}"
            sleep(1000)
            log.debug "Promise 2 - end"
            //no return
        }.then {
            throw new Exception("Test")
        }.onError {error ->
            log.error("Catched")
        }.go()

        log.debug "Started"
        while (!p.isCompleted()){
            sleep(500)
            log.debug "wait"
        }
        log.debug("done")
    }
}

