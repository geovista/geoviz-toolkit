/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Masahiro Takatsuka */

package geovista.common.data;

import java.util.Random;

public final class MtRandom extends Random {

	private MtRandom() {
	}

	public static void shuffle(double v[]) {
		int n = v.length;
		for (int i = n - 1; i > 0; i--) {
			int j = (int) ((i + 1) * Math.random());
			double t = v[i];
			v[i] = v[j];
			v[j] = t;
		}

	}

	public static void shuffle(int v[]) {
		int n = v.length;
		for (int i = n - 1; i > 0; i--) {
			int j = (int) ((i + 1) * Math.random());
			int t = v[i];
			v[i] = v[j];
			v[j] = t;
		}

	}

	public static int[] randomPermutations(int n) {
		int v[] = new int[n];
		for (int i = 0; i < n; i++) {
			v[i] = i + 1;
		}

		shuffle(v);
		return v;
	}

	public static double random(double min, double max) {
		return min + Math.random() * (max - min);
	}

	public static double gaussianRandom(double std) {
		if (flag_gaussian) {
			flag_gaussian = false;
			return std * k * Math.sin(q);
		}
		flag_gaussian = true;
		k = Math.sqrt(-2D * Math.log(1.0D - Math.random()));
		q = 6.2831853071795862D * Math.random();
		return std * k * Math.cos(q);
	}

	public static double gammaRandom(double p) {
		double x;
		if (p > 1.0D) {
			double t = Math.sqrt(2D * p - 1.0D);
			double u;
			double y;
			do {
				do {
					do {
						x = 1.0D - Math.random();
						y = 2D * Math.random() - 1.0D;
					} while (x * x + y * y > 1.0D);
					y /= x;
					x = (t * y + p) - 1.0D;
				} while (x <= 0.0D);
				u = (p - 1.0D) * Math.log(x / (p - 1.0D)) - t * y;
			} while (u < -50D || Math.random() > (1.0D + y * y) * Math.exp(u));
		} else {
			double t = 2.7182818284590451D / (p + 2.7182818284590451D);
			double y;
			do {
				if (Math.random() < t) {
					x = Math.pow(Math.random(), 1.0D / p);
					y = Math.exp(-x);
				} else {
					x = 1.0D - Math.log(Math.random());
					y = Math.pow(x, p - 1.0D);
				}
			} while (Math.random() >= y);
		}
		return x;
	}

	public static double betaRandom(double p, double q) {
		double t = gammaRandom(p);
		return t / (t + gammaRandom(q));
	}

	public static int poissonRandom(double n) {
		int k = 0;
		for (n = Math.exp(n) * Math.random(); n > 1.0D;) {
			n *= Math.random();
			k++;
		}

		return k;
	}

	public static double weibullRandom(double alpha) {
		return Math.pow(-Math.log(1.0D - Math.random()), 1.0D / alpha);
	}

	public static double chiSquareRandom(double n) {
		return 2D * gammaRandom(0.5D * n);
	}

	public static double tRandom(double n) {
		if (n <= 2D) {
			return Math.random() / Math.sqrt(chiSquareRandom(n) / n);
		}
		double a;
		double b;
		double c;
		if (n < 6D) {
			do {
				a = Math.random();
				b = (a * a) / (n - 2D);
				c = Math.log(1.0D - Math.random()) / (1.0D - 0.5D * n);
			} while (b >= 1.0D || Math.exp(-b - c) > 1.0D - b);
		} else {
			do {
				a = Math.random();
				b = (a * a) / (n - 2D);
				c = Math.log(1.0D - Math.random()) / (1.0D - 0.5D * n);
			} while (Math.exp(-b - c) > 1.0D - b);
		}
		return a / Math.sqrt((1.0D - 2D / n) * (1.0D - b));
	}

	public static double fRandom(double n1, double n2) {
		return (chiSquareRandom(n1) * n2) / (chiSquareRandom(n2) * n1);
	}

	public static double logisticRandom() {
		double r = Math.random();
		return Math.log(r / (1.0D - r));
	}

	public static double powerRandom(int n) {
		return Math.pow(Math.random(), 1.0D / (n + 1));
	}

	public static double[] binormalRandom(double c) {
		double xy[] = new double[2];
		double r1;
		double r2;
		double s;
		do {
			r1 = 2D * Math.random() - 1.0D;
			r2 = 2D * Math.random() - 1.0D;
			s = r1 * r1 + r2 * r2;
		} while (s > 1.0D || s == 0.0D);
		s = -Math.log(s) / s;
		r1 = Math.sqrt((1.0D + c) * s) * r1;
		r2 = Math.sqrt((1.0D - c) * s) * r2;
		xy[0] = r1 + r2;
		xy[1] = r1 - r2;
		return xy;
	}

	public static int binomialRandom(int n, double p) {
		int r = 0;
		for (int i = 0; i < n; i++) {
			if (Math.random() < p) {
				r++;
			}
		}

		return r;
	}

	public static double[] unitSphereRandom(int n) {
		double v[] = new double[n];
		double r = 0.0D;
		if (n < 5) {
			do {
				for (int i = 0; i < n; i++) {
					v[i] = 2D * Math.random() - 1.0D;
					r += v[i] * v[i];
				}

			} while (r > 1.0D);
		} else {
			// MtRandom random = new MtRandom();
			for (int i = 0; i < n; i++) {
				v[i] = gaussianRandom(1.0D);
				r += v[i] * v[i];
			}

		}
		r = Math.sqrt(r);
		for (int i = 0; i < n; i++) {
			v[i] /= r;
		}

		return v;
	}

	public static double exponentialRandom(double m) {
		return m * -Math.log(1.0D - Math.random());
	}

	public static double geometricRandom(double p) {
		if (p > 0.20000000000000001D) {
			int n;

			for (n = 1; Math.random() > p; n++) {

			}
			return n;
		}
		return Math.ceil(Math.log(1.0D - Math.random()) / Math.log(1.0D - p));
	}

	public static double cauchyRandom() {
		double X;
		double Y;
		do {
			X = Math.random();
			Y = 2D * Math.random() - 1.0D;
		} while (Math.sqrt(X) + Math.sqrt(Y) > 1.0D);
		return Y / X;
	}

	public static double triangularRandom() {
		return Math.random() - Math.random();
	}

	private static boolean flag_gaussian = false;
	private static double k;
	private static double q;

}
