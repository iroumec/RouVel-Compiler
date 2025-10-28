package semantic;

/**
 * Este 'record' se justifica en que los puntos de bifurcación que se prometen
 * pueden completarse en un futuro en el que la cantidad actual de separaciones
 * no es la que había al momento en el que se realizó la promesa. Como
 * resultado, el índice de la polaca podría ser erróneo.
 */
public record Promise(int bifurcationPoint, int separations) {

}
