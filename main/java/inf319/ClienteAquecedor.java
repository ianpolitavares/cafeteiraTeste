package inf319;

/**
 * Interface para todos os clientes do aquecedor de uma
 * cafeteira. Clientes do aquecedor podem ser informados da presença ou
 * ausência da jarra, ou se o ciclo já terminou ou não. cicloCompleto
 * significa que uma jarra vazia foi colocada no aquecedor após o café
 * ter sido feito.
 */
public interface ClienteAquecedor {
    public void jarra();
    public void semJarra();
    public void cicloCompleto();
}
