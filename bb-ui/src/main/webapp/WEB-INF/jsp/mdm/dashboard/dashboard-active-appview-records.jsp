<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:forEach items="${activerecordlist}" var="active">
	<tr class="odd">
		<td>US</td>
		<td>123456781234567</td>
		<td>AK34567891-345678</td>
		<td>Sept 10 , 2014</td>
		<td>Apple</td>
		<td>US First Filling</td>
		<td>John Mayer<br />Oct 10, 2014
		</td>
		<td>
			<div class="action-btns-grid">
				<a href=""><span class="icon icon-edit"></span> Edit</a>
				<div class="dropdown grid-dropdown">
					<a id="drop4" href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false"><img src="images/svg/change-status.svg"
						class="icon20x"> Change Status <span class="caret"></span> </a>
					<ul id="menu1" class="dropdown-menu" aria-labelledby="drop4">
						<li><a href="#">Transfer Record</a></li>
						<li><a href="#">Allowed to Abandon</a></li>
						<li><a href="#">Switch Off</a></li>
						<li><a href="#">Delete</a></li>
					</ul>
				</div>
			</div>
		</td>
	</tr>
</c:forEach>