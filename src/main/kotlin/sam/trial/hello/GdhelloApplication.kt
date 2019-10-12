package sam.trial.hello

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GdhelloApplication

fun main(args: Array<String>) {
	runApplication<GdhelloApplication>(*args)
}
