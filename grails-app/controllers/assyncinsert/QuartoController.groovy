package assyncinsert

import org.springframework.dao.DataIntegrityViolationException

class QuartoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [quartoInstanceList: Quarto.list(params), quartoInstanceTotal: Quarto.count()]
    }

    def create() {
        [quartoInstance: new Quarto(params)]
    }

    def save() {
        def quartoInstance = new Quarto(params)
        if (!quartoInstance.save(flush: true)) {
            render(view: "create", model: [quartoInstance: quartoInstance])
            return
        }

        event("salvarUnidades", quartoInstance)

        flash.message = message(code: 'default.created.message', args: [message(code: 'quarto.label', default: 'Quarto'), quartoInstance.id])
        redirect(action: "show", id: quartoInstance.id)
    }

    def show(Long id) {
        def quartoInstance = Quarto.get(id)
        if (!quartoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quarto.label', default: 'Quarto'), id])
            redirect(action: "list")
            return
        }

        [quartoInstance: quartoInstance]
    }

    def edit(Long id) {
        def quartoInstance = Quarto.get(id)
        if (!quartoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quarto.label', default: 'Quarto'), id])
            redirect(action: "list")
            return
        }

        [quartoInstance: quartoInstance]
    }

    def update(Long id, Long version) {
        def quartoInstance = Quarto.get(id)
        if (!quartoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quarto.label', default: 'Quarto'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (quartoInstance.version > version) {
                quartoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'quarto.label', default: 'Quarto')] as Object[],
                          "Another user has updated this Quarto while you were editing")
                render(view: "edit", model: [quartoInstance: quartoInstance])
                return
            }
        }

        quartoInstance.properties = params

        if (!quartoInstance.save(flush: true)) {
            render(view: "edit", model: [quartoInstance: quartoInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'quarto.label', default: 'Quarto'), quartoInstance.id])
        redirect(action: "show", id: quartoInstance.id)
    }

    def delete(Long id) {
        def quartoInstance = Quarto.get(id)
        if (!quartoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quarto.label', default: 'Quarto'), id])
            redirect(action: "list")
            return
        }

        try {
            quartoInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'quarto.label', default: 'Quarto'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'quarto.label', default: 'Quarto'), id])
            redirect(action: "show", id: id)
        }
    }
}
