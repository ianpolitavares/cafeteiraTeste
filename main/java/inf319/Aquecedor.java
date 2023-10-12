package inf319;

/**
 * Esta classe implementa o controle do sistema do aquecedor da cafeteira
 * Harar. O aquecedor informa se está pronto para começar a
 * fazer café e é informado que a preparação do café terminou.
 */
public class Aquecedor {
    
    private Hardware         hardware;
    private ClienteAquecedor cliente;
    private EstadoAquecedor  estado;

    public Aquecedor(Hardware oHardware, 
		     ClienteAquecedor oCliente) {
	hardware = oHardware;
        cliente = oCliente;
        estado = EstadoAquecedor.naoAquecendo;
        hardware.atuElementoAquecedor(EstadoHardware.aquecedorDesligado);
    }

    
    // Estímulos externos
    public void fazerCafe() {
        if (estado.equals(EstadoAquecedor.naoAquecendo)) {
            estado = EstadoAquecedor.fazendoJarVazia;
        }
    }

    public void cafeFeito() {
        if (estado.equals(EstadoAquecedor.fazendoJarVazia)) {
            estado = EstadoAquecedor.naoAquecendo;
            cliente.cicloCompleto();
        } else if (estado.equals(EstadoAquecedor.fazendoAquecendo)) {
            estado = EstadoAquecedor.cafeFeito;
        } else if (estado.equals(EstadoAquecedor.jarVaziaRemovida)) {
            estado = EstadoAquecedor.naoAquecendo;
            cliente.cicloCompleto();
        } else if (estado.equals(EstadoAquecedor.jarCheiaRemovida)) {
            estado = EstadoAquecedor.feitoJarRemovida;
        }
    }

    public boolean checaPronto() {
        return hardware.leEstadoAquecedor().equals(EstadoHardware.jarraVazia);
    }
    

    // Eventos da máquina de estados
    public void jarraVazia() {
        if (estado.equals(EstadoAquecedor.fazendoAquecendo)) {
            estado = EstadoAquecedor.fazendoJarVazia;
            hardware.atuElementoAquecedor(EstadoHardware.aquecedorDesligado);
        } else if (estado.equals(EstadoAquecedor.jarVaziaRemovida)
                || estado.equals(EstadoAquecedor.jarCheiaRemovida)) {
            estado = EstadoAquecedor.fazendoJarVazia;
            cliente.jarra();
        } else if (estado.equals(EstadoAquecedor.cafeFeito)) {
            estado = EstadoAquecedor.naoAquecendo;
            hardware.atuElementoAquecedor(EstadoHardware.aquecedorDesligado);
            cliente.cicloCompleto();
        } else if (estado.equals(EstadoAquecedor.feitoJarRemovida)) {
            estado = EstadoAquecedor.naoAquecendo;
            cliente.cicloCompleto();
        }
    }

    public void jarraNaoVazia() {
        if (estado.equals(EstadoAquecedor.fazendoJarVazia)) {
            estado = EstadoAquecedor.fazendoAquecendo;
            hardware.atuElementoAquecedor(EstadoHardware.aquecedorLigado);
        } else if (estado.equals(EstadoAquecedor.jarVaziaRemovida)
                || estado.equals(EstadoAquecedor.jarCheiaRemovida)) {
            estado = EstadoAquecedor.fazendoAquecendo;
            hardware.atuElementoAquecedor(EstadoHardware.aquecedorLigado);
            cliente.jarra();
        } else if (estado.equals(EstadoAquecedor.feitoJarRemovida)) {
            estado = EstadoAquecedor.cafeFeito;
            hardware.atuElementoAquecedor(EstadoHardware.aquecedorLigado);
        }
    }

    public void placaVazia() {
        if (estado.equals(EstadoAquecedor.fazendoJarVazia)) {
            estado = EstadoAquecedor.jarVaziaRemovida;
            cliente.semJarra();
        } else if (estado.equals(EstadoAquecedor.fazendoAquecendo)) {
            estado = EstadoAquecedor.jarCheiaRemovida;
            cliente.semJarra();
            hardware.atuElementoAquecedor(EstadoHardware.aquecedorDesligado);
        } else if (estado.equals(EstadoAquecedor.cafeFeito)) {
            estado = EstadoAquecedor.feitoJarRemovida;
            hardware.atuElementoAquecedor(EstadoHardware.aquecedorDesligado);
        }
    }
    

    // Ligação ao programa principal
    public void verifica() {
        if (hardware.leEstadoAquecedor().equals(EstadoHardware.placaVazia)) {
            placaVazia();
        } else if (hardware.leEstadoAquecedor().equals(
                EstadoHardware.jarraVazia)) {
            jarraVazia();
        } else if (hardware.leEstadoAquecedor().equals(
                EstadoHardware.jarraNaoVazia)) {
            jarraNaoVazia();
        }
    }
}

class EstadoAquecedor {
    
    // Estados da máquina de estados do aquecedor
    public static final EstadoAquecedor naoAquecendo =      new EstadoAquecedor(0);
    public static final EstadoAquecedor fazendoJarVazia= new EstadoAquecedor(1); 
    public static final EstadoAquecedor fazendoAquecendo=new EstadoAquecedor(2); 
    public static final EstadoAquecedor jarVaziaRemovida=new EstadoAquecedor(3);
    public static final EstadoAquecedor jarCheiaRemovida=new EstadoAquecedor(4); 
    public static final EstadoAquecedor cafeFeito=       new EstadoAquecedor(5); 
    public static final EstadoAquecedor feitoJarRemovida=new EstadoAquecedor(6); 
    
    private int id;
    
    private EstadoAquecedor(int id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof EstadoAquecedor)
                && ((EstadoAquecedor) obj).id == id;
    }
}
