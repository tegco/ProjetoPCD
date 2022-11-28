package game;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player extends Thread  {

	protected  Game game;

	int id;

	private byte currentStrength;
	protected byte originalStrength;

	public Cell getCurrentCell() {
		return this.game.getCell(game.searchPlayerInBoard(this));
	}


	public Player(int id, Game game, byte strength) {
		super();
		this.id = id;
		this.game=game;
		currentStrength=strength;
		originalStrength=strength;
	}


	// Generate a player's initial strength randomly, from 1 to 3 (inclusive)
	public static int generateOriginalStrength() {

		Random random = new Random();

		int originalStrength = random.nextInt(3 - 1 + 1) + 1;
		//System.out.println("Energy:" + originalStrength);
		return originalStrength;
	}


	public void movementOutcome(Player otherPlayer) throws InterruptedException {

		byte otherPlayerStrength = otherPlayer.getCurrentStrength();

		//Se for um player ativo
		if (otherPlayer.isActive()) {

			System.err.println("Player# " + this.getIdentification() + " e Player# " + otherPlayer.getIdentification()  + " confrontation!!!" );
			setAfterConfrontationStrength(this, otherPlayer);

			//System.out.println("Player#" + otherPlayer.getIdentification() + " " +  "ativo");
			this.getCurrentCell().setPlayer(this);
			//this.wait();
			//otherPlayer.wait();
			otherPlayer.getCurrentCell().setPlayer(otherPlayer);

			//o vencedor esta a ficar bloqueado

		}
		//Se for um jogador morto
		if (otherPlayer.isDead()) {

			System.out.println("MOVIMENTO DEVE SER BLOQUEADO - JOGADOR MORTO " + otherPlayer.toString());
			this.wait();
		}

		if (this.hasMaxStrenght()) {
			System.out.println(this.toString() + "ATINGOU PONTUAÇÃO MÁXIMA");
		}	
	}

	// -> FUNCIONA
	public static Player confrontationWinner(Player p1, Player p2) {

		if (p1.currentStrength > p2.currentStrength) {
			return p1;

		}

		if (p1.currentStrength == p2.currentStrength) {

			Random random = new Random();
			int randomPlayer = random.nextInt(2);

			if (randomPlayer == 0) {
				return p1;
			}
			return p2;

		}
		return p2;
	}

	// -> FUNCIONA
	public static void setAfterConfrontationStrength (Player p1, Player p2) {

		Player winner = confrontationWinner(p1, p2);

		System.out.println("Winner is Player#" + winner.getIdentification());

		int s = p1.currentStrength += p2.currentStrength;

		if (s > 10) {
			s = 10;
		}

		if (p1 == winner) {

			p1.currentStrength = (byte) s;
			p2.currentStrength = 0;

			//p2.interrupt();
		}

		else {
			p2.currentStrength = (byte) s;
			p1.currentStrength = 0;

			//p1.interrupt();
		}
		System.out.println("Player#" + p1.getIdentification() + ": " + p1.currentStrength + "; " + "Player#" + p2.getIdentification() + ": " + p2.currentStrength);

	}

	public boolean isValidPosition (Coordinate newCoord) {

		if (game.isWithinBounds(newCoord)) {
			return true;
		}

		return false;
	}

	public abstract void move(Direction direction) throws InterruptedException;

	public abstract boolean isHumanPlayer();

	public boolean hasMaxStrenght () {
		return this.currentStrength == Game.MAX_POSSIBLE_STRENGTH;
	}

	public boolean isActive () {
		return (this.currentStrength > 0 && this.currentStrength < 10);

	}

	public boolean isDead() {
		return this.currentStrength == 0;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", currentStrength=" + currentStrength + ", getCurrentCell()=" + getCurrentCell()
		+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public byte getCurrentStrength() {
		return currentStrength;
	}

	public int getIdentification() {
		return id;
	}
}
