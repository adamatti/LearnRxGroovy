package adamatti

import groovy.util.logging.Slf4j
import rx.Observable

@Slf4j
class Sample1 {
    public static void main(String [] args){
        Observable.from(1..10).forEach{arg ->
		    Thread.sleep(1000)
			log.info ("Subscribe ${arg}")
		}
        log.info("End")
	}
}
