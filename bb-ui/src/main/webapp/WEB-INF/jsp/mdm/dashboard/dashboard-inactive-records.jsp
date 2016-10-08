<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:forEach items="${inactiverecordlist}" var="inactive">
	<tr class="odd">
		<td class="text-center"><span class="icon icon-plus"></span></td>
		<td><a href="javascript:void(0)">F002345</a></td>
		<td>US</td>
		<td>123456781234567</td>
		<td>AK34567891-345678</td>
		<td>Sept 10 , 2014</td>
		<td>Apple</td>
		<td>US First Filling</td>
		<td>
			<div>John Mayer</div>
			<div>
				Oct 10, 2014 <span class="icon icon-comment pull-right"></span>
			</div>
		</td>
		<td>Dropped</td>
	</tr>
</c:forEach>