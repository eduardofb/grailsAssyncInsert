package assyncinsert

class Quarto {

	static hasMany = [unidades: Unidade]

	List<Unidade> unidades;

	String nome;

    static constraints = {
    }
}
