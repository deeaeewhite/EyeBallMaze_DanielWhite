package nz.ac.arastudent.eyeballmazeassignment2.model;

class CoOrds {
	int x, y;
	PlayerDirection looking;
	
	CoOrds(int p1, int p2, PlayerDirection dir){
		x = p1;
		y = p2;
		looking = dir;
	}
}
