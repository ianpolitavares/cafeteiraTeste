package inf319;

/** 
 * Esta classe implementa o modelo de controle do vaporizador
 * da cafeteira Harar.
 */
public class Vaporizador {
    
    private Hardware           hardware;
    private ClienteVaporizador cliente;
    private EstadoVaporizador  estado;

    public Vaporizador(Hardware oHardware, ClienteVaporizador oCliente) {
	hardware = oHardware;
        cliente = oCliente;
        estado = EstadoVaporizador.naoFazendo;
        hardware.atuEstadoElementoEbulidor(EstadoHardware.ebulidorDesligado);
        hardware.atuValvulaPressao(EstadoHardware.valvulaFechada);
    }


    // Estímulos externos
    public boolean checaPronto() {
        return 
            hardware.leEstadoEbulidor().equals(EstadoHardware.ebulidorNaoVazio);
    }

    public void jarra() {
        if (estado.equals(EstadoVaporizador.fazendoJarRemovida)) {
            estado = EstadoVaporizador.vaporizando;
            hardware.atuEstadoElementoEbulidor(EstadoHardware.ebulidorLigado);
            hardware.atuValvulaPressao(EstadoHardware.valvulaFechada);
        }
    }

    public void semJarra() {
        if (estado.equals(EstadoVaporizador.vaporizando)) {
            estado = EstadoVaporizador.fazendoJarRemovida;
            hardware.atuEstadoElementoEbulidor(EstadoHardware.ebulidorDesligado);
            hardware.atuValvulaPressao(EstadoHardware.valvulaAberta);
        }
    }

    public void fazerCafe() {
        if (estado.equals(EstadoVaporizador.naoFazendo)) {
            estado = EstadoVaporizador.vaporizando;
            hardware.atuEstadoElementoEbulidor(EstadoHardware.ebulidorLigado);
            hardware.atuValvulaPressao(EstadoHardware.valvulaFechada);
        }
    }
    

    // Eventos da máquina de estados
    public void cafeFeito() {
        if (estado.equals(EstadoVaporizador.vaporizando)) {
            estado = EstadoVaporizador.naoFazendo;
            hardware.atuEstadoElementoEbulidor(EstadoHardware.ebulidorDesligado);
            hardware.atuValvulaPressao(EstadoHardware.valvulaFechada);
            cliente.cafeFeito();
        } else if (estado.equals(EstadoVaporizador.fazendoJarRemovida)) {
            estado = EstadoVaporizador.naoFazendo;
            cliente.cafeFeito();
        } 
    }

    
    // Ligação ao programa principal
    public void verifica() {
        if (!hardware.leEstadoEbulidor().equals(
                EstadoHardware.ebulidorNaoVazio)) {
            cafeFeito();
        }
    }
}

class EstadoVaporizador {
    
    // Estados da máquina de estados do vaporizador
    public static final EstadoVaporizador naoFazendo= 
        new EstadoVaporizador(0);
    public static final EstadoVaporizador vaporizando=
        new EstadoVaporizador(1);
    public static final EstadoVaporizador fazendoJarRemovida= 
        new EstadoVaporizador(2);
    
    private int id;
    
    private EstadoVaporizador(int id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof EstadoVaporizador)
                && ((EstadoVaporizador) obj).id == id;
    }
}
