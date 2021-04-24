<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.5" tiledversion="1.5.0" name="dungeon_" tilewidth="16" tileheight="16" tilecount="180" columns="15">
 <image source="dungeon_/dungeon_/dungeon_.png" width="240" height="192"/>
 <tile id="0" type="Walls"/>
 <tile id="1" type="Walls"/>
 <tile id="2" type="Walls"/>
 <tile id="3" type="Walls"/>
 <tile id="4" type="Walls"/>
 <tile id="5" type="Walls"/>
 <tile id="8" type="Walls"/>
 <tile id="9" type="Walls"/>
 <tile id="10" type="Walls"/>
 <tile id="11" type="Walls"/>
 <tile id="12" type="Walls"/>
 <tile id="15" type="Walls"/>
 <tile id="16" type="Walls"/>
 <tile id="17" type="Walls"/>
 <tile id="18" type="Walls"/>
 <tile id="19" type="Walls"/>
 <tile id="20" type="Walls"/>
 <tile id="21" type="Walls"/>
 <tile id="22" type="Walls"/>
 <tile id="23" type="Walls"/>
 <tile id="24" type="Walls"/>
 <tile id="25" type="Walls"/>
 <tile id="26" type="Walls"/>
 <tile id="27" type="Walls"/>
 <tile id="30" type="Walls"/>
 <tile id="31" type="Walls"/>
 <tile id="32" type="Walls"/>
 <tile id="33" type="Walls"/>
 <tile id="35" type="Walls"/>
 <tile id="36" type="Walls"/>
 <tile id="37" type="Walls"/>
 <tile id="38" type="Walls"/>
 <tile id="39" type="Walls"/>
 <tile id="40" type="Walls"/>
 <tile id="41" type="Walls"/>
 <tile id="42" type="Walls"/>
 <tile id="45" type="Walls"/>
 <tile id="46" type="Walls"/>
 <tile id="47" type="Walls"/>
 <tile id="48" type="Walls"/>
 <tile id="50" type="Walls"/>
 <tile id="51" type="Walls"/>
 <tile id="52" type="Walls"/>
 <tile id="53" type="Walls"/>
 <tile id="54" type="Walls"/>
 <tile id="60" type="Walls"/>
 <tile id="61" type="Walls"/>
 <tile id="62" type="Walls"/>
 <tile id="63" type="Walls"/>
 <tile id="75" type="Door">
  <properties>
   <property name="Collision" type="bool" value="true"/>
  </properties>
  <animation>
   <frame tileid="75" duration="500"/>
   <frame tileid="76" duration="500"/>
   <frame tileid="77" duration="500"/>
   <frame tileid="78" duration="500"/>
  </animation>
 </tile>
 <tile id="76" type="Door">
  <properties>
   <property name="Collision" type="bool" value="true"/>
  </properties>
 </tile>
 <tile id="77" type="Door">
  <properties>
   <property name="Collision" type="bool" value="true"/>
  </properties>
 </tile>
 <tile id="78" type="Door">
  <properties>
   <property name="Collision" type="bool" value="true"/>
  </properties>
 </tile>
 <tile id="90" type="Walls">
  <animation>
   <frame tileid="90" duration="500"/>
   <frame tileid="91" duration="500"/>
   <frame tileid="92" duration="500"/>
   <frame tileid="93" duration="500"/>
  </animation>
 </tile>
 <tile id="91" type="Walls"/>
 <tile id="92" type="Walls"/>
 <tile id="93" type="Walls"/>
 <tile id="105" type="Spikes">
  <animation>
   <frame tileid="105" duration="500"/>
   <frame tileid="106" duration="500"/>
   <frame tileid="107" duration="500"/>
   <frame tileid="108" duration="500"/>
   <frame tileid="109" duration="500"/>
   <frame tileid="110" duration="500"/>
  </animation>
 </tile>
 <tile id="120">
  <animation>
   <frame tileid="120" duration="500"/>
   <frame tileid="121" duration="500"/>
   <frame tileid="122" duration="500"/>
   <frame tileid="123" duration="500"/>
   <frame tileid="124" duration="500"/>
   <frame tileid="125" duration="500"/>
  </animation>
 </tile>
 <tile id="135" type="Walls"/>
 <tile id="136" type="Walls"/>
 <tile id="137" type="Walls"/>
 <tile id="152" type="Stairs">
  <properties>
   <property name="Downstairs" type="bool" value="true"/>
  </properties>
 </tile>
 <tile id="153" type="Walls"/>
 <tile id="165" type="Stairs"/>
 <tile id="166" type="Stairs"/>
 <tile id="167" type="Stairs">
  <properties>
   <property name="Downstairs" type="bool" value="true"/>
  </properties>
 </tile>
 <tile id="168" type="Stairs">
  <properties>
   <property name="Downstairs" type="bool" value="true"/>
  </properties>
 </tile>
 <wangsets>
  <wangset name="Dungeon" type="mixed" tile="-1">
   <wangcolor name="Wall" color="#ff0000" tile="-1" probability="1"/>
   <wangcolor name="Void" color="#00ff00" tile="-1" probability="1"/>
   <wangtile tileid="0" wangid="1,1,0,2,0,1,1,1"/>
   <wangtile tileid="1" wangid="1,1,0,2,2,2,0,1"/>
   <wangtile tileid="2" wangid="1,1,1,1,0,2,0,1"/>
   <wangtile tileid="3" wangid="1,1,1,1,0,1,1,1"/>
   <wangtile tileid="4" wangid="2,2,2,1,2,2,2,2"/>
   <wangtile tileid="5" wangid="2,2,2,2,2,1,2,2"/>
   <wangtile tileid="8" wangid="2,2,2,1,2,1,1,1"/>
   <wangtile tileid="9" wangid="2,1,1,1,2,1,2,2"/>
   <wangtile tileid="10" wangid="1,1,2,1,2,1,1,1"/>
   <wangtile tileid="11" wangid="1,1,2,1,2,1,2,1"/>
   <wangtile tileid="12" wangid="1,1,1,1,2,1,2,1"/>
   <wangtile tileid="15" wangid="0,2,2,2,0,1,1,1"/>
   <wangtile tileid="16" wangid="2,2,2,2,2,2,2,2"/>
   <wangtile tileid="17" wangid="0,1,1,1,0,2,2,2"/>
   <wangtile tileid="18" wangid="0,1,1,1,0,1,1,1"/>
   <wangtile tileid="19" wangid="2,1,2,2,2,2,2,2"/>
   <wangtile tileid="20" wangid="2,2,2,2,2,2,2,1"/>
   <wangtile tileid="21" wangid="2,2,2,2,2,1,2,1"/>
   <wangtile tileid="22" wangid="2,1,2,1,2,2,2,2"/>
   <wangtile tileid="23" wangid="2,1,2,2,2,1,1,1"/>
   <wangtile tileid="24" wangid="2,1,1,1,2,2,2,1"/>
   <wangtile tileid="25" wangid="2,1,2,1,2,1,1,1"/>
   <wangtile tileid="26" wangid="2,1,2,1,2,1,2,1"/>
   <wangtile tileid="27" wangid="2,1,1,1,2,1,2,1"/>
   <wangtile tileid="30" wangid="0,2,0,1,1,1,1,1"/>
   <wangtile tileid="31" wangid="2,2,0,1,1,1,0,2"/>
   <wangtile tileid="32" wangid="0,1,1,1,1,1,0,2"/>
   <wangtile tileid="33" wangid="0,1,1,1,1,1,1,1"/>
   <wangtile tileid="35" wangid="2,2,2,1,2,1,2,2"/>
   <wangtile tileid="36" wangid="2,1,2,2,2,1,2,1"/>
   <wangtile tileid="37" wangid="2,1,2,1,2,2,2,1"/>
   <wangtile tileid="38" wangid="1,1,2,1,2,2,2,1"/>
   <wangtile tileid="39" wangid="1,1,2,2,2,1,2,1"/>
   <wangtile tileid="40" wangid="2,1,2,1,1,1,1,1"/>
   <wangtile tileid="41" wangid="2,1,2,1,1,1,2,1"/>
   <wangtile tileid="42" wangid="2,1,1,1,1,1,2,1"/>
   <wangtile tileid="45" wangid="1,1,0,1,1,1,1,1"/>
   <wangtile tileid="46" wangid="1,1,0,1,1,1,0,1"/>
   <wangtile tileid="47" wangid="1,1,1,1,1,1,0,1"/>
   <wangtile tileid="48" wangid="1,1,1,1,1,1,1,1"/>
   <wangtile tileid="50" wangid="2,1,2,2,2,2,2,1"/>
   <wangtile tileid="51" wangid="2,2,2,1,2,1,2,1"/>
   <wangtile tileid="52" wangid="2,1,2,1,2,1,2,2"/>
   <wangtile tileid="53" wangid="2,1,2,1,1,1,2,2"/>
   <wangtile tileid="54" wangid="2,2,2,1,1,1,2,1"/>
  </wangset>
  <wangset name="Carpet" type="mixed" tile="-1">
   <wangcolor name="red" color="#ff0000" tile="-1" probability="1"/>
   <wangtile tileid="55" wangid="0,0,1,1,1,0,0,0"/>
   <wangtile tileid="56" wangid="0,0,1,1,1,1,1,0"/>
   <wangtile tileid="57" wangid="0,0,0,0,1,1,1,0"/>
   <wangtile tileid="58" wangid="1,1,1,0,1,1,1,1"/>
   <wangtile tileid="59" wangid="1,1,1,1,1,0,1,1"/>
   <wangtile tileid="70" wangid="1,1,1,1,1,0,0,0"/>
   <wangtile tileid="71" wangid="1,1,1,1,1,1,1,1"/>
   <wangtile tileid="72" wangid="1,0,0,0,1,1,1,1"/>
   <wangtile tileid="73" wangid="1,0,1,1,1,1,1,1"/>
   <wangtile tileid="74" wangid="1,1,1,1,1,1,1,0"/>
   <wangtile tileid="85" wangid="1,1,1,0,0,0,0,0"/>
   <wangtile tileid="86" wangid="1,1,1,0,0,0,1,1"/>
   <wangtile tileid="87" wangid="1,0,0,0,0,0,1,1"/>
  </wangset>
  <wangset name="Stone Floor" type="mixed" tile="-1">
   <wangcolor name="dark edge" color="#ff0000" tile="-1" probability="1"/>
   <wangcolor name="light edge" color="#00ff00" tile="-1" probability="1"/>
   <wangtile tileid="65" wangid="1,2,2,2,0,1,1,1"/>
   <wangtile tileid="66" wangid="1,1,0,2,0,1,1,1"/>
   <wangtile tileid="67" wangid="1,2,2,2,2,2,0,1"/>
   <wangtile tileid="68" wangid="1,1,0,2,2,2,1,1"/>
   <wangtile tileid="69" wangid="1,2,2,2,0,2,0,1"/>
   <wangtile tileid="79" wangid="1,2,2,2,2,2,1,1"/>
   <wangtile tileid="80" wangid="0,2,2,2,2,2,1,1"/>
   <wangtile tileid="81" wangid="0,2,2,2,2,2,1,1"/>
   <wangtile tileid="82" wangid="1,1,0,0,0,1,1,1"/>
   <wangtile tileid="83" wangid="1,2,2,2,0,0,0,1"/>
   <wangtile tileid="84" wangid="0,2,2,2,2,2,1,1"/>
   <wangtile tileid="94" wangid="1,1,0,2,2,2,1,1"/>
   <wangtile tileid="95" wangid="1,2,2,2,2,2,0,1"/>
   <wangtile tileid="96" wangid="1,2,2,2,0,1,1,1"/>
   <wangtile tileid="97" wangid="0,0,0,2,2,2,1,1"/>
   <wangtile tileid="98" wangid="0,2,2,2,2,2,0,0"/>
   <wangtile tileid="99" wangid="1,2,2,2,0,1,1,1"/>
   <wangtile tileid="111" wangid="0,2,0,2,2,2,1,1"/>
   <wangtile tileid="112" wangid="1,2,2,2,2,2,0,1"/>
   <wangtile tileid="113" wangid="1,1,0,2,2,2,1,1"/>
   <wangtile tileid="114" wangid="0,2,2,2,2,2,0,1"/>
  </wangset>
 </wangsets>
</tileset>
