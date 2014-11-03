package assyncinsert

import grails.events.Listener
import org.grails.plugin.platform.events.EventMessage
import assyncinsert.Quarto

class UnidadeService {


	@Listener(topic="salvarUnidades", namespace="app")
    def salvarUnidades(EventMessage<Quarto> message) {
    	def quarto = Quarto.get(message.data.id)
    	if(quarto) {	
    		(1..10).each {
    			new Unidade(nome: "Test ${it}", quarto: quarto.save()).save(failOnError: true)
    		}
    	}
    }
}
