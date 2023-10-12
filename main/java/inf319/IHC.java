package inf319;

/** 
 * Esta classe implementa o controle da IHC da cafeteira Harar.
 * Esta classe conhece clientes da IHC e o controlador do hardware. Ela
 * permite que classes externas notifiquem o estado do ciclo de confecção
 * do café.
 */
public class IHC {
    
    private Hardware   hardware;
    private ClienteIHC cliente;
    private EstadoIHC  estado;

    public IHC(Hardware oHardware, ClienteIHC oCliente) {
	hardware = oHardware;
        cliente = oCliente;
        estado = EstadoIHC.naoFazendo;
        hardware.atuLuzIndicadora(EstadoHardware.indicadoraDesligada);
    }

    
    // Estímulos externos
    public void cafeFeito() {
        if (estado.equals(EstadoIHC.verificandoProntidao))
            estado = EstadoIHC.naoFazendo;
        else if (estado.equals(EstadoIHC.fazendo)) {
            estado = EstadoIHC.cafeFeito;
            hardware.atuLuzIndicadora(EstadoHardware.indicadoraLigada);
        }
    }
    
    public void cicloCompleto() {
        if (estado.equals(EstadoIHC.verificandoProntidao))
            estado = EstadoIHC.naoFazendo;
        else if (estado.equals(EstadoIHC.fazendo))
            estado = EstadoIHC.naoFazendo;
        else if (estado.equals(EstadoIHC.cafeFeito)) {
            estado = EstadoIHC.naoFazendo;
            hardware.atuLuzIndicadora(EstadoHardware.indicadoraDesligada);
        }
    }
    

    // Eventos da máquina de estados
    public void inicio() {
        if (estado.equals(EstadoIHC.naoFazendo)) {
            estado = EstadoIHC.verificandoProntidao;
            verificaPronto();
        }
    }

    public void pronto() {
        if (estado.equals(EstadoIHC.verificandoProntidao)) {
            estado = EstadoIHC.fazendo;
            cliente.fazerCafe();
        }
    }

    public void naoPronto() {
        if (estado.equals(EstadoIHC.verificandoProntidao)) {
            estado = EstadoIHC.naoFazendo;
        }
    }

    // Método auxiliar
    private void verificaPronto() {
        if (cliente.checaPronto()) {
            pronto();
        } else {
            naoPronto();
        }
    }
    
    
    // Ligação ao programa principal
    public void verifica() {
        if ((estado.equals(EstadoIHC.naoFazendo))
                && (hardware.leEstadoInterruptor().equals(
                        EstadoHardware.interruptorPressionado))) {
            inicio();
        }
    }
}

class EstadoIHC {
    
    // Estados da máquina de estados da IHC
    public static final EstadoIHC naoFazendo=           new EstadoIHC(0); 
    public static final EstadoIHC fazendo=              new EstadoIHC(1); 
    public static final EstadoIHC verificandoProntidao= new EstadoIHC(2); 
    public static final EstadoIHC cafeFeito=            new EstadoIHC(3);
    
    private int id;

    private EstadoIHC(int id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof EstadoIHC)
                && ((EstadoIHC) obj).id == id;
    }
}
