package inf319;

/**
 * Classe que engloba os estados dos componentes de hardware.
 */
public class EstadoHardware {
    
    public static final EstadoHardware jarraNaoVazia =          new EstadoHardware(0);
    public static final EstadoHardware jarraVazia =             new EstadoHardware(1);
    public static final EstadoHardware placaVazia =             new EstadoHardware(2);
    public static final EstadoHardware ebulidorVazio =          new EstadoHardware(3);
    public static final EstadoHardware ebulidorNaoVazio =       new EstadoHardware(4);
    public static final EstadoHardware interruptorPressionado = new EstadoHardware(5);
    public static final EstadoHardware interruptorSolto =       new EstadoHardware(6);
    public static final EstadoHardware ebulidorLigado =         new EstadoHardware(7);
    public static final EstadoHardware ebulidorDesligado =      new EstadoHardware(8);
    public static final EstadoHardware aquecedorLigado =        new EstadoHardware(9);
    public static final EstadoHardware aquecedorDesligado =     new EstadoHardware(10);
    public static final EstadoHardware indicadoraLigada =       new EstadoHardware(11);
    public static final EstadoHardware indicadoraDesligada =    new EstadoHardware(12);
    public static final EstadoHardware indicadoraCoacao =       new EstadoHardware(13);
    public static final EstadoHardware valvulaAberta =          new EstadoHardware(14);
    public static final EstadoHardware valvulaFechada =         new EstadoHardware(15);

    
    private int id;

    private EstadoHardware(int id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof EstadoHardware)
                && ((EstadoHardware) obj).id == id;
    }
}
