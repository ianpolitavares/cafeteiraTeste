package inf319;

/**
 * Esta classe implementa a cafeteira Harar.
 * Esta classe é o centro da comunicação entre o aquecedor, vaporizador e
 * IHC. Ela implementa todas as três interfaces de clientes e direciona
 * as mensagens entre estas abstrações.
 */
public class Cafeteira 
        implements ClienteVaporizador, ClienteAquecedor, ClienteIHC {
    
    private IHC         suaIHC;
    private Aquecedor   seuAquecedor;
    private Vaporizador seuVaporizador;

    public Cafeteira() {
        suaIHC = null;
        seuAquecedor = null;
        seuVaporizador = null;
    }

    
    // ClienteIHC
    public boolean checaPronto() {
        return seuAquecedor.checaPronto() && seuVaporizador.checaPronto();
    }

    public void fazerCafe() {
        seuAquecedor.fazerCafe();
        seuVaporizador.fazerCafe();
    }
    
    
    // ClienteVaporizador
    public void cafeFeito() {
        suaIHC.cafeFeito();
        seuAquecedor.cafeFeito();
    } 


    // ClienteAquecedor
    public void jarra() {
        seuVaporizador.jarra();
    }

    public void semJarra() {
        seuVaporizador.semJarra();
    }

    public void cicloCompleto() {
        suaIHC.cicloCompleto();
    }

    
    // Métodos para 'montar' a cafeteira. 
    public void ajustaIHC(IHC aIHC) {
        suaIHC = aIHC;
    }

    public void ajustaAquecedor(Aquecedor oAquecedor) {
        seuAquecedor = oAquecedor;
    }

    public void ajustaVaporizador(Vaporizador oVaporizador) {
        seuVaporizador = oVaporizador;
    }

    
    public static void main(String[] args) {
        // Cria os componentes da cafeteira
        Hardware oHardware = new Hardware();
        Cafeteira aCafeteira = new Cafeteira();

        Aquecedor aquecedor = new Aquecedor(oHardware, aCafeteira);
        Vaporizador vaporizador = new Vaporizador(oHardware, aCafeteira);
        IHC ihc = new IHC(oHardware, aCafeteira);

        // Liga os componentes ao modulo de controle
        aCafeteira.ajustaIHC(ihc);
        aCafeteira.ajustaVaporizador(vaporizador);
        aCafeteira.ajustaAquecedor(aquecedor);

        
        //////////////////////////////////////////////////////////////////
        // Executa a cafeteira

        // Inicia o hardware
        oHardware.iniciar();

        // Executa o software de controle
        while (true) {
            ihc.verifica();
            vaporizador.verifica();
            aquecedor.verifica();
            try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
}
