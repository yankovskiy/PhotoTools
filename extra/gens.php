<?php
$list = "samsungMilc";
$vendor = "Vendor.SAMSUNG_MILC";

function g($name, $senWidth, $resWidth) {
	global $list, $vendor;
	printf("%s.add(new CameraData(%s, \"%s\", new BigDecimal(\"%.1f\"), new BigDecimal(\"%d\")));\n", $list, $vendor, $name, $senWidth, $resWidth);
}

printf("List<CameraData> %s = new ArrayList<>();\n", $list);

printf("DATABASE.put(%s, %s);\n", $vendor, $list);