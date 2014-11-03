package assyncinsert



import org.junit.*
import grails.test.mixin.*

@TestFor(QuartoController)
@Mock(Quarto)
class QuartoControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/quarto/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.quartoInstanceList.size() == 0
        assert model.quartoInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.quartoInstance != null
    }

    void testSave() {
        controller.save()

        assert model.quartoInstance != null
        assert view == '/quarto/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/quarto/show/1'
        assert controller.flash.message != null
        assert Quarto.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/quarto/list'

        populateValidParams(params)
        def quarto = new Quarto(params)

        assert quarto.save() != null

        params.id = quarto.id

        def model = controller.show()

        assert model.quartoInstance == quarto
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/quarto/list'

        populateValidParams(params)
        def quarto = new Quarto(params)

        assert quarto.save() != null

        params.id = quarto.id

        def model = controller.edit()

        assert model.quartoInstance == quarto
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/quarto/list'

        response.reset()

        populateValidParams(params)
        def quarto = new Quarto(params)

        assert quarto.save() != null

        // test invalid parameters in update
        params.id = quarto.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/quarto/edit"
        assert model.quartoInstance != null

        quarto.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/quarto/show/$quarto.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        quarto.clearErrors()

        populateValidParams(params)
        params.id = quarto.id
        params.version = -1
        controller.update()

        assert view == "/quarto/edit"
        assert model.quartoInstance != null
        assert model.quartoInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/quarto/list'

        response.reset()

        populateValidParams(params)
        def quarto = new Quarto(params)

        assert quarto.save() != null
        assert Quarto.count() == 1

        params.id = quarto.id

        controller.delete()

        assert Quarto.count() == 0
        assert Quarto.get(quarto.id) == null
        assert response.redirectedUrl == '/quarto/list'
    }
}
