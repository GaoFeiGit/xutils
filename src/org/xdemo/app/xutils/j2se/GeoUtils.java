package org.xdemo.app.xutils.j2se;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年8月21日 下午7:03:16
 */
public class GeoUtils {
	/**
	 * 判断某个点，是否在某个区域内 java.awt.geom 提供用于在与二维几何形状相关的对象上定义和执行操作的 Java 2D 类。
	 * 
	 * @param point
	 *            目标点
	 * @param points
	 *            点集合，形成闭合区域
	 * @return
	 */
	public static boolean isInPolygon(Double[] point, Double[]... points) {
		if (point.length != 2)
			return false;
		if (points.length < 3)
			return false;

		Point2D.Double _point = new Point2D.Double(point[0], point[1]);
		List<Point2D.Double> _points = new ArrayList<Point2D.Double>();

		for (Double[] _p : points) {
			if (_p.length != 2)
				return false;
			_points.add(new Point2D.Double(_p[0], _p[1]));
		}

		return isInPolygon(_point, _points);
	}

	/**
	 * 判断某个点，是否在某个区域内 java.awt.geom 提供用于在与二维几何形状相关的对象上定义和执行操作的 Java 2D 类。
	 * 
	 * @param point
	 *            目标点
	 * @param points
	 *            点集合，形成闭合区域
	 * @return
	 */
	public static boolean isInPolygon(Point2D.Double point, List<Point2D.Double> points) {

		int N = points.size();
		boolean boundOrVertex = true; // 如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
		int intersectCount = 0;// cross points count of x
		double precision = 2e-10; // 浮点类型计算时候与0比较时候的容差
		Point2D.Double p1, p2;// neighbour bound vertices
		Point2D.Double p = point; // 当前点

		p1 = points.get(0);// left vertex
		for (int i = 1; i <= N; ++i) {// check all rays
			if (p.equals(p1)) {
				return boundOrVertex;// p is an vertex
			}

			p2 = points.get(i % N);// right vertex
			if (p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)) {// ray
																			// is
																			// outside
																			// of
																			// our
																			// interests
				p1 = p2;
				continue;// next ray left point
			}

			if (p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)) {// ray
																			// is
																			// crossing
																			// over
																			// by
																			// the
																			// algorithm
																			// (common
																			// part
																			// of)
				if (p.y <= Math.max(p1.y, p2.y)) {// x is before of ray
					if (p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)) {// overlies
																		// on a
																		// horizontal
																		// ray
						return boundOrVertex;
					}

					if (p1.y == p2.y) {// ray is vertical
						if (p1.y == p.y) {// overlies on a vertical ray
							return boundOrVertex;
						} else {// before ray
							++intersectCount;
						}
					} else {// cross point on the left side
						double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;// cross
																								// point
																								// of
																								// y
						if (Math.abs(p.y - xinters) < precision) {// overlies on
																	// a ray
							return boundOrVertex;
						}

						if (p.y < xinters) {// before ray
							++intersectCount;
						}
					}
				}
			} else {// special case when ray is crossing through the vertex
				if (p.x == p2.x && p.y <= p2.y) {// p crossing over p2
					Point2D.Double p3 = points.get((i + 1) % N); // next vertex
					if (p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)) {// p.x
																						// lies
																						// between
																						// p1.x
																						// &
																						// p3.x
						++intersectCount;
					} else {
						intersectCount += 2;
					}
				}
			}
			p1 = p2;// next ray left point
		}

		if (intersectCount % 2 == 0) {// 偶数在多边形外
			return false;
		} else { // 奇数在多边形内
			return true;
		}
	}

	/**
	 * 地球的半径有以下三个常用值：
	 * 极半径
	 * 从地心到北极或南极的距离,大约6356.公里（两极的差极小,可以忽略）.
	 * 赤道半径
	 * 是从地心到赤道的距离,大约6378公里.
	 * 平均半径
	 * 大约6371..这个数字是地心到地球表面所有各点距离的平均值.
	 */
	private static double EARTH_RADIUS = 6378.137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 根据两个位置的经纬度，来计算两地的距离（单位为KM） 参数为String类型
	 * 
	 * @param lat1
	 *            第一个点经度
	 * @param lng1
	 *            第一个点纬度
	 * @param lat2
	 *            第二个点经度
	 * @param lng2
	 *            第二个点纬
	 * @return
	 */
	public static Double getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str) {
		Double lat1 = Double.parseDouble(lat1Str);
		Double lng1 = Double.parseDouble(lng1Str);
		Double lat2 = Double.parseDouble(lat2Str);
		Double lng2 = Double.parseDouble(lng2Str);
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double difference = radLat1 - radLat2;
		double mdifference = rad(lng1) - rad(lng2);
		double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(mdifference / 2), 2)));
		distance = distance * EARTH_RADIUS;
		distance = Math.round(distance * 10000) / 10000;
		return distance;
	}}
