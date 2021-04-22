<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.5" tiledversion="1.5.0" name="dungeon_" tilewidth="16" tileheight="16" tilecount="180" columns="15">
 <image source="dungeon_/dungeon_/dungeon_.png" width="240" height="192"/>
 <tile id="75">
  <animation>
   <frame tileid="75" duration="500"/>
   <frame tileid="76" duration="500"/>
   <frame tileid="77" duration="500"/>
   <frame tileid="78" duration="500"/>
  </animation>
 </tile>
 <tile id="90">
  <animation>
   <frame tileid="90" duration="500"/>
   <frame tileid="91" duration="500"/>
   <frame tileid="92" duration="500"/>
   <frame tileid="93" duration="500"/>
  </animation>
 </tile>
 <tile id="105">
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
 <wangsets>
  <wangset name="Dungeon" type="mixed" tile="-1">
   <wangcolor name="Wall" color="#ff0000" tile="-1" probability="1"/>
   <wangcolor name="Floor" color="#0000ff" tile="-1" probability="10"/>
   <wangtile tileid="0" wangid="1,1,0,0,0,1,1,1"/>
   <wangtile tileid="1" wangid="1,1,0,0,0,0,0,1"/>
   <wangtile tileid="2" wangid="1,1,1,1,0,0,0,1"/>
   <wangtile tileid="3" wangid="1,1,1,1,0,1,1,1"/>
   <wangtile tileid="4" wangid="0,0,0,1,0,0,0,0"/>
   <wangtile tileid="5" wangid="0,0,0,0,0,1,0,0"/>
   <wangtile tileid="8" wangid="0,0,0,1,0,1,1,1"/>
   <wangtile tileid="9" wangid="0,1,1,1,0,1,0,0"/>
   <wangtile tileid="10" wangid="1,1,0,1,0,1,1,1"/>
   <wangtile tileid="11" wangid="1,1,0,1,0,1,0,1"/>
   <wangtile tileid="12" wangid="1,1,1,1,0,1,0,1"/>
   <wangtile tileid="15" wangid="0,0,0,0,0,1,1,1"/>
   <wangtile tileid="17" wangid="0,1,1,1,0,0,0,0"/>
   <wangtile tileid="18" wangid="0,1,1,1,0,1,1,1"/>
   <wangtile tileid="19" wangid="0,1,0,0,0,0,0,0"/>
   <wangtile tileid="20" wangid="0,0,0,0,0,0,0,1"/>
   <wangtile tileid="21" wangid="0,0,0,0,0,1,0,1"/>
   <wangtile tileid="22" wangid="0,1,0,1,0,0,0,0"/>
   <wangtile tileid="23" wangid="0,1,0,0,0,1,1,1"/>
   <wangtile tileid="24" wangid="0,1,1,1,0,0,0,1"/>
   <wangtile tileid="25" wangid="0,1,0,1,0,1,1,1"/>
   <wangtile tileid="26" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="27" wangid="0,1,1,1,0,1,0,1"/>
   <wangtile tileid="30" wangid="0,0,0,1,1,1,1,1"/>
   <wangtile tileid="31" wangid="0,0,0,1,1,1,0,0"/>
   <wangtile tileid="32" wangid="0,1,1,1,1,1,0,0"/>
   <wangtile tileid="33" wangid="0,1,1,1,1,1,1,1"/>
   <wangtile tileid="35" wangid="0,0,0,1,0,1,0,0"/>
   <wangtile tileid="36" wangid="0,1,0,0,0,1,0,1"/>
   <wangtile tileid="37" wangid="0,1,0,1,0,0,0,1"/>
   <wangtile tileid="38" wangid="1,1,0,1,0,0,0,1"/>
   <wangtile tileid="39" wangid="1,1,0,0,0,1,0,1"/>
   <wangtile tileid="40" wangid="0,1,0,1,1,1,1,1"/>
   <wangtile tileid="41" wangid="0,1,0,1,1,1,0,1"/>
   <wangtile tileid="42" wangid="0,1,1,1,1,1,0,1"/>
   <wangtile tileid="45" wangid="1,1,0,1,1,1,1,1"/>
   <wangtile tileid="46" wangid="1,1,0,1,1,1,0,1"/>
   <wangtile tileid="47" wangid="1,1,1,1,1,1,0,1"/>
   <wangtile tileid="48" wangid="0,1,0,1,0,1,0,1"/>
   <wangtile tileid="50" wangid="0,1,0,0,0,0,0,1"/>
   <wangtile tileid="51" wangid="0,0,0,1,0,1,0,1"/>
   <wangtile tileid="52" wangid="0,1,0,1,0,1,0,0"/>
   <wangtile tileid="53" wangid="0,1,0,1,1,1,0,0"/>
   <wangtile tileid="54" wangid="0,0,0,1,1,1,0,1"/>
   <wangtile tileid="79" wangid="2,2,2,2,2,2,2,2"/>
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
 </wangsets>
</tileset>
