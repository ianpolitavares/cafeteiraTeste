package inf319;

/**
 * Classe que implementa a interface e encarna o 'hardware' da cafeteira.
 */
public class Hardware {

    // Estados dos componentes do hardware
    private EstadoHardware estadoAquecedor;
    private EstadoHardware estadoEbulidor;
    private EstadoHardware estadoInterruptor;
    private EstadoHardware estadoElementoEbulidor;
    private EstadoHardware estadoElementoAquecedor;
    private EstadoHardware estadoLuzIndicadora;
    private EstadoHardware estadoValvulaPressao;

    // Estado dos reservatórios
    private int nivelDeAgua;
    private int nivelDeCafe;

    // Janela da interface
    private JanelaCafeteira jc;

    /**
     * Construtor define o estado da maquina ao ser 'ligada' e é independente do
     * estado inicial do software de controle.
     */
    public Hardware() {
        estadoAquecedor = EstadoHardware.jarraVazia;
        estadoEbulidor = EstadoHardware.ebulidorVazio;
        estadoInterruptor = EstadoHardware.interruptorSolto;
        estadoElementoEbulidor = EstadoHardware.ebulidorDesligado;
        estadoElementoAquecedor = EstadoHardware.aquecedorDesligado;
        estadoLuzIndicadora = EstadoHardware.indicadoraLigada;
        estadoValvulaPressao = EstadoHardware.valvulaFechada;

        nivelDeAgua = 0;
        nivelDeCafe = 0;
  
        jc = new JanelaCafeteira(this);
    }


    // Métodos que implementam a interface do hardware. Esta
    // interface é usada pelo software de controle.

    // Aquecedor
    public EstadoHardware leEstadoAquecedor() {
        return estadoAquecedor;
    }

    public void atuElementoAquecedor(EstadoHardware modo) {
        if (modo.equals(EstadoHardware.aquecedorLigado)
                || modo.equals(EstadoHardware.aquecedorDesligado)) {
            estadoElementoAquecedor = modo;     
            jc.atualizaEstado();
        }
    }

    // Ebulidor
    public EstadoHardware leEstadoEbulidor() {
        return estadoEbulidor;
    }

    public void atuEstadoElementoEbulidor(EstadoHardware modo) {
        if (modo.equals(EstadoHardware.ebulidorLigado)
                || modo.equals(EstadoHardware.ebulidorDesligado)) {
            estadoElementoEbulidor = modo;
            jc.atualizaEstado();
        }
    }

    // Interruptor
    public EstadoHardware leEstadoInterruptor() {
        EstadoHardware modo = estadoInterruptor;
        estadoInterruptor = EstadoHardware.interruptorSolto;
        return modo;
    }

    // Luz indicadora
    public void atuLuzIndicadora(EstadoHardware modo) {
        if (modo.equals(EstadoHardware.indicadoraLigada)
                || modo.equals(EstadoHardware.indicadoraDesligada)
                || modo.equals(EstadoHardware.indicadoraCoacao)) {
            estadoLuzIndicadora = modo;
            jc.atualizaEstado();
        }
    }

    // Válvula de pressão
    public void atuValvulaPressao(EstadoHardware modo) {
        if (modo.equals(EstadoHardware.valvulaAberta)
                || modo.equals(EstadoHardware.valvulaFechada)) {
            estadoValvulaPressao = modo;
            jc.atualizaEstado();
        }
    }


    // Metodos usados na 'implementação' do harware. São usados pela
    // interface gráfica.
    public EstadoHardware leEstadoLuzIndicadora() {
        return estadoLuzIndicadora;
    }

    public EstadoHardware leEstadoElementoEbulidor() {
        return estadoElementoEbulidor;
    }

    public EstadoHardware leEstadoValvulaPressao() {
        return estadoValvulaPressao;
    }

    public EstadoHardware leEstadoElementoAquecedor() {
        return estadoElementoAquecedor;
    }

    public void colocaJarra() {
        if (nivelDeCafe == 0) {
            estadoAquecedor = EstadoHardware.jarraVazia;
        } else {
            estadoAquecedor = EstadoHardware.jarraNaoVazia;
        }
    }

    public void removeJarra() {
        estadoAquecedor = EstadoHardware.placaVazia;
    }

    public void pressionaBotao() {
        estadoInterruptor = EstadoHardware.interruptorPressionado;
    }

    public synchronized int pegaNivelDeAgua() {
        return nivelDeAgua;
    }

    public synchronized int pegaNivelDeCafe() {
        return nivelDeCafe;
    }

    public synchronized void ajustaNivelDeAgua(int nivel) {
        if ((nivel >= 0) && (nivel <= 100)) {
            nivelDeAgua = nivel;
            if (nivelDeAgua == 0) {
                estadoEbulidor = EstadoHardware.ebulidorVazio;
            } else {
                estadoEbulidor = EstadoHardware.ebulidorNaoVazio;
            }
            jc.atualizaEstado();
        }
    }

    public synchronized void ajustaNivelDeCafe(int nivel) {
        if ((nivel >= 0) && (nivel <= 100)) {
        	nivelDeCafe = nivel;
            if (!estadoAquecedor.equals(EstadoHardware.placaVazia)) {
                if (nivelDeCafe == 0) {
                    estadoAquecedor = EstadoHardware.jarraVazia;
                } else {
                    estadoAquecedor = EstadoHardware.jarraNaoVazia;
                }
            }
        }
        jc.atualizaEstado();
    }


    // Implementação do 'funcionamento' do hardware. É totalmente
    // independente do software de controle e engloba a interface
    // gráfica.
    public synchronized void fazCafe() {
        if (estadoEbulidor.equals(EstadoHardware.ebulidorNaoVazio)
                && estadoElementoEbulidor.equals(EstadoHardware.ebulidorLigado)
                && estadoValvulaPressao.equals(EstadoHardware.valvulaFechada)) {
            ajustaNivelDeAgua(pegaNivelDeAgua() - 1);
            ajustaNivelDeCafe(pegaNivelDeCafe() + 1);
        }
    }
    
    public void iniciar() {
        // Inicia a ebulição da água
        new Microcontrolador(this).start();
        // Inicia a interface gráfica
        jc.preparaJanela();
    }
}

class Microcontrolador extends Thread {
    
    private Hardware cafeteira;

    public Microcontrolador(Hardware oHardware) {
        cafeteira = oHardware;
    }

    public void run() {
        try {
            while (true) {
                sleep(500);
                cafeteira.fazCafe();
            }
        } catch (InterruptedException e) {
        }
    }
}
