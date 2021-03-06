package com.rs.game.player.content.custom;
import java.io.File;
import java.io.Serializable;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.World;
import com.rs.game.player.Player;
import com.rs.utils.Logger;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

/**
 * @author KingFox
 */
public class ItemManager implements Serializable {
	private static final long serialVersionUID = -9028730399175475751L;
	
	public int itemId;
	public int value;
	public String itemName;
	
	public static ItemManager[] values;
	
	public ItemManager(int itemid, int value, String name) {
		this.itemId = itemid;
		this.value = value;
		this.itemName = name;
	}

	private static final String PATH = "data/ItemPrices.ser";
	
	public static void inits() {
		File file = new File(PATH);
		if (file.exists())
			try {
				values = (ItemManager[]) SerializableFilesManager.loadSerializedFile(file);
				return;
			} catch (Throwable e) {
				Logger.handle(e);
			}
		values = new ItemManager[30000];
	}
	
	public static final void save() {
		try {
			SerializableFilesManager.storeSerializableClass(values, new File(PATH));
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}
	
	public static String getName(int itemId) {
		return values[itemId].itemName;
	}
	
	public static boolean update(int itemId, int newPrice) {
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null)
				continue;
			if (values[i].itemId == itemId) {
				values[i].value = newPrice;
			}
		}
		save();
		return true;
	}
	
	
	public static int getPrice(int itemId) {
		for (int i = 0; i < values.length; i++) {
			if (ItemManager.values[i] == null) {
				continue;
			}
			if (ItemManager.values[i].itemId == itemId) {
				return values[i].value;
			}
		}switch (itemId) {
		//customs
		case 29033:
		case 29036:
		case 29039:
		case 29042:
		case 29069:
		case 29072:
		case 29075:
			return 10000000;
		case 950://silk
			return 1500;
		case 29034:
		case 29037:
		case 29040:
		case 29044:
		case 29070:
		case 29073:
		case 29076:
			return 1;
		case 29035:
		case 29038:
		case 29041:
		case 29043:
		case 29071:
		case 29074:
		case 29077:
			return 1;
		case 28817:
			return 1;
		case 28818:
			return 1;
		case 28819:
		case 28820:
		case 28823:
		case 28916:
		case 28917:
		case 28918:
			return 1;
		case 28834:
		case 28835:
		case 28836:
		case 28837:
		case 28871:
		case 28838:
		case 28840:
		case 28841:
			return 1;
		case 28826:
		case 28829:
		case 28831:
		case 28832:
		case 28833:
		case 28843:
			return 1;
		case 28822:
		case 29031:
		case 29032:
			return 1;
		
		case 28830:
		case 28850:
		case 28969:
		case 28970:
		case 29084:
		case 29091:
			return 1;
		case 29063:
		case 29092:
		case 29095:
		case 28908:
		case 28909:
		case 28919:
		case 28956:
			return 1;
		case 28961:
		case 28962:
		case 28963:
		case 28964:
		case 28965:
		case 28966:
		case 28882:
		case 28967:
		case 28968:
			return 1;
		case 28910:
		case 28911:
		case 28912:
		case 28913:
			return 1;
		case 29104:
		case 28875:
		case 28942:
		case 28943:
		case 28944:
		case 28945:
		case 28946:
		case 28947:
			return 1;
		case 28950:
		case 28951:
		case 29105:
		case 28851:
			return 1;	
		case 28849:
		case 28854:
		case 28855:
			return 1;
		case 28856:
		case 28857:
		case 28858:
		case 28859:
		case 28860:
			return 1;
		case 28861:
		case 28862:
		case 28868:
		case 28971:
		case 28972:
		case 29062:
		case 29093:
		case 29097:
		case 29098:
			return 1;
		case 28844:
		case 28845:
		case 28848:
			return 1;
		case 28948:
		case 28949:
			return 1;
		case 28794:
		case 28795:
		case 28796:
		case 28797:
		case 28798:
		case 28799:
		return 10;
		case 25825: 
		case 25827: 
		case 25829: 
		case 25831: 
		case 25833: 
		case 25835: 
		case 25837: 
		case 25839: 
		case 25841: 
		case 25843: 
		case 25845: 
		case 25847: 
		case 25849: 
		case 25851: 
		case 25853: 
		return 15;
		case 960: 
		return 1500;
		case 989:
			return 10;	
		case 19275: 
case 19278:
case 19284:
case 19287:
case 19290:
case 19293:
case 19296:
case 19299:
case 19302:
case 19305:
case 19368:
case 19370:
case 19372:
case 19374:
case 19376:
case 19378:
case 19380:
case 19382:
case 19384:
case 19386:
case 19388:
case 19390:
case 19392:
case 19394:
case 19396:
case 10446:
case 10448:
case 10450:
case 10452:
case 10454:
case 10456:
case 10458:
case 10460:
case 10462:
case 10464:
case 10466:
case 10468:
case 10470:
case 10472:
case 10474:
return 5500000;
case 28997: 
case 28998: 
case 28999: 
case 29000: 
case 29001: 
case 29002: 
case 29003:  
case 29004: 
case 29005: 
case 29006: 
case 29007: 
case 29085: 
case 29086: 
case 29087: 
case 29088:
return 8500000;
case 28869: 
case 28870: 
case 28880: 
case 29030: 
case 29045: 
case 28821: 
case 29079: 
case 29080: 
case 29081: 
case 29082:
return 15500000;
case 29026:
case 29027:
case 28953: 
case 28955: 
case 28824:
case 28825: 
case 28827: 
case 28828: 
case 28865:
return 15500000;
case 28872:
case 28873: 
case 28874: 
case 28852:
case 29053: 
case 29054: 
case 29055: 
case 29056: 
case 29057: 
case 29058: 
case 29059: 
case 29060:
case 28801: 
case 28802: 
case 28803: 
case 28804: 
case 28805: 
case 29012: 
case 29013: 
case 29014: 
case 29015: 
return 435500000;
		case 14878:
		case 14880:
		case 14881:
		case 14882:
		case 14883:
		case 14892:
		case 14893:
		case 14894:
		case 14895:
		case 14896:
		case 14897:
		case 14898:
		case 14899:
		case 14902:
		case 14903:
		case 14905:
		case 14906:
		case 14907:
		case 14908:
		case 14909:
			return 7500000;
		case 29016:
			return 500000000;
		case 29017:
			return 250000000;
		case 29018:
			return 50000000;
		case 29019:
			return 10000000;
		case 29020:
			return 1000000;
		case 29021:
			return 1000000000;
		case 9772:	
		case 9778:
		case 9784:
		case 9790:
		case 9796:
		case 9802:
		case 9808:
		case 9949:
		case 9814:
		case 10662:
		case 10647:
		case 10648:
		case 10649:
		case 10650:
		case 12171:
		case 10651:
		case 10652:
		case 10653:
		case 10654:
		case 10655:
		case 10656:
		case 10657:
		case 10658:
		case 10659:
		case 10660:
		case 9948:
		case 9950:	
		case 9812:
		case 9809:
		case 9806:
		case 9800:
		case 9794:
		case 9788:
		case 9785:
		case 9782:
		case 9779:
		case 9776:
		case 9770:
		case 9774:
		case 9771:
		case 9780:
		case 9786:
		case 9792:
		case 9798:
		case 9804:
		case 9810:
			return 99000;
		case 804:
			return 750;			
		case 805:
			return 1050;
		case 806:
			return 10;			
		case 10498:
		case 10499:
			return 1000000;	
		case 5680:
			return 32000;	
		case 5698:
			return 62000;	
		case 10348:
		case 10350:
		return 350000000;
		case 10340:
		case 10342:
		return 150000000;
		case 5318: //potato
		return 19;		
		case 5319: // onion
		return 18;
		case 5324: // cabbage
		return 210;
		case 5322: // tomato
		return 60;
		case 5320: //Sweetcorn
		return 18;
		case 5323: // Strawberry
		return 1869;
		case 5321: //watermelon
		return 10443;
		case 5295: //ranaar
		return 1372;
		case 5296: //Toadflax seed
		return 2050;
		case 5297: //irit seed
		return 257;
		case 5298: //avantoe
		return 3727;
		case 28883:
		case 28884:
		case 28885:
		case 28886:
		case 28887:
		case 28888:
		case 28889:
		case 28890:
		case 28891:
		case 28892:
		case 28893:
		case 28894:
		case 28895:
		case 28896:
		case 28897:
		case 28898:
		case 28899:
		case 28900:
		case 28901:
		case 28902:
		case 28903:
		case 28904:
		case 28905:
		case 28906:
		case 28907:
		return 3500000;
		case 19907:
		return 7000;
		case 19912:
		return 7000;
		case 19917:
		return 7000;
		case 23745:
			return 2500000;		
		case 23746:
			return 3000000;		
		case 23747:
			return 4000000;		
		case 23748:
			return 5000000;		
		case 24108:
		case 24110:
		case 24112:
		case 24114:
			return 100000;	
		case 13101:
			return 50000;		
		case 23750:
			return 100;		
		case 23751:
			return 150;		
		case 23752:
			return 200;	
		case 20822:
		case 20823:
		case 20824:
		case 20825:
		case 20826:
			return 1250;	
		case 20768:
		case 20767:
			return 2500000;
		case 18510:
		case 18508:
		case 18509:
		case 19709:
			return 99000;	
		case 25666:
		case 25638:
		case 22478:
		case 25879:
		case 22474:
		case 22470:
		case 22450:
			return 50000;	
		case 2572:
			return 500000;	
		case 22494:
			return 100000;	
		case 22449:
		case 22466:
		case 22462:
		case 22458:
			return 25000;	
		case 22451:
		case 22482:
		case 22486:
		case 22490:
		case 25978:
		case 25980:
			return 250000;	
		case 22899:
		case 22905:
		case 23848:
			return 2500;	
		case 22901:
		case 22907:
		case 23850:
			return 5000;
		case 22903:
		case 22909:
		case 23852:
			return 10000;
		case 21537:
		case 21539:
		case 21541:
		case 21545:
			return 1000000;
		case 23876:
		case 23874:
		case 23854:
			return 15000;
		case 24365:
			return 5000000;	
		case 24439:
		case 24438:
		case 24440:
		case 24441:
		case 25031:
		case 25034:
			return 20000000;	
		case 16955:
		case 16403:
		case 16425:
		case 16909:
		case 15888:
		case 17143:
		case 17039:
		case 16381:
			return 1000;	
		case 18363:
			return 75000;	
		case 18361:
			return 50000;		
		case 18347:
			return 50000;	
		case 19669:
			return 75000;	
		case 18335:
		case 18346:
		case 19893:
		case 19892:
		case 19888:
		case 19887:
		case 18334:
		case 18333:
			return 50000;	
		case 25028:
		case 16995:
			return 7500000;	
		case 18744:
		case 18745:
		case 18746:
			return 20;	
		case 21472:
		case 21473:
		case 21474:
		case 21475:
		case 21476:
			return 50000;	
		case 13672:
		case 13673:
		case 13674:
		case 13675:
			return 500000;	
		case 6137:
        case 6139:
		case 6141:
		case 6153:
		case 6147:
			return 500000;		
		case 24354:
		case 24356:
		case 24357:
		case 24358:
			return 500000;		
		case 24359:
		case 24360:
		case 24363:
		case 24361:
		case 24362:
			return 1000000;	
		case 24100:
		case 24102:
		case 24104:
		case 24106:
			return 50000;	
		case 3753:
		case 3755:
		case 3751:
		case 3749:
			return 750000;	
		case 10344:
		case 10336:
			return 45000000;	
		case 15272:
			return 950;	
		case 20949:
		case 20950:
		case 20951:
		case 20952:
			return 10;	
		case 22207:
		case 22209:
		case 22211:
		case 22213:
		    return 10;	
		case 6737:
		case 6731:
		case 6735:
		case 6733:
			return 7000000;    
		case 15243:
			return 1500;    
		case 14497:
		case 14499:
		case 14501:
		    return 2500000;    
		case 15444:
		case 15443:
		case 15442:
		case 15441:
			return 10000000;	
		case 10548:
			return 150;
		case 13263:
		   	return 5000000;	
		case 4675:
			return 500000;	
		case 24092:
		case 24094:
		case 24096:
		case 24098:
		    return 4;    
		case 22362:
		case 22363:
		case 22364:
		case 22365:
		case 22358:
		case 22359:
		case 22360:
		case 22361:
			return 25000000;	
		case 1712:
			return 50000;	
		case 10551:
			return 150;	
		case 14636:
		case 22528:
		case 22534:
		case 22540:
		case 22546:
		case 22548:
		case 22542:
		case 22536:
		case 22530:
		case 15492:
		   	return 5000000;
		case 25202:
			return 750000000;	
		case 24167:
			return 350000000;	
		case 21773:
			return 2000;	
		case 21777:
			return 25000000;
		case 8849:
			return 5000;		
		case 10828:
		   	return 50000;	
		case 8848:
			return 5000000;		
		case 8847:
		case 8846:
		case 8845:
		case 8844:
			return 50000;
		case 2412:
		case 2413:
		case 2414:
			return 50000;		
		case 21790:
			return 20000000;		
		case 23531:
			return 8000;		
		case 23621:
			return 5000;	
		case 23351:
			return 8000;	
	    case 6685:
	    case 6686:
			return 3000;	
       case 21512:
        	return 1000000;
        case 12196:
        case 22992:
        case 24511:
        case 24512:
			return 10000;		
		case 4722:
		case 4720:
		case 4718:
		case 4716:
		case 4753:
		case 4755:
		case 4757:
		case 4759:
		case 4724:
		case 4726:
		case 4728:
		case 4730:
		case 4745:
		case 4747:
		case 4749:
		case 4751:
		case 4712:
		case 4714:
		case 4710:
		case 4708:
		case 4736:
		case 4738:
		case 4732:
		case 4734:
			return 15000000;		
		case 6914:
			return 10000000;		
		case 8839:
		case 8840:
			return 1000;	
		case 8842:
			return 750;		
		case 10611:
			return 15000000;		
		case 11663:
		case 11664:
		case 11665:
			return 950;		
		case 6746:
		case 13734:
			return 50000000;		
		case 3481:
        case 3483:
        case 3485:
        case 3486:
        case 3488:
			return 3000000;		
		case 11716:
		    return 80000000;		
		case 6889:
			return 7500000;		
		case 3105:
			return 50000;		
		case 15126:
			return 15000000;		
		case 3842:
		case 3840:
		    return 20000000;		
		case 1755:
		case 1592:
		case 1597:
			return 65000;	
		case 5341:
			return 50;	
		case 9244:
			return 7500;	
		case 10008:
		case 10006:
		    return 15000;		
		case 2577:
			return 16000000;		
		case 2581:
			return 15000000;	
		case 11235:
			return 30000000;	
		case 15486:
		case 15487:
		case 15502:
			return 10000000;	
		case 15332:
			return 50000;	
		case 6918:
		case 6920:
		case 6916:
		case 6922:
		case 6924:
			return 15000000;	
		case 324:
		case 1386:
			return 200;
		case 11335:
			return 30000000;
		case 14479:
			return 25000000;
		case 3140:
			return 15000000;
		case 11732:
			return 5000000;	
		case 4087:
		case 4585:
			return 10000000;	
		case 1187:
			return 6000000;	
		case 4151:
			return 10000000;	
		case 20671:
			return 3500000;	
		case 21371:
			return 85000000;	
		case 13754:
			return 500000000;	
		case 23679:
			return 95500000;
		case 1231:
			return 50000;
		case 13958:
			return 10000000;	
		case 13961:
		case 13964:
		case 13967:		
		case 13970:
		case 13973:
		case 13858:
		case 13861:	
		case 13864:
		case 13867:
		case 13884:
		case 13887:
		case 13890:
		case 13893:
		case 13896:
		case 13899:
		case 13902:
		case 13905:
		case 13908:
		case 13911:
		case 13914:
		case 13917:
		case 13920:
		case 13923:
		case 13926:
		case 13929:
		case 13932:
		case 13935:
		case 13938:
		case 13941:
		case 13944:
		case 13947:
		case 13950:
			return 10000000;	
		case 13876:
		case 13870:
		case 13873:
			return 1000000;	
		case 7462:
			return 800;	
		case 20072:
			return 750;	
		case 7461:
			return 750;	
		case 8850:
			return 500;
		case 7460:
			return 700;	
		case 7459:
			return 650;	
		case 7458:
			return 500;	
		case 7457:
			return 450;	
		case 7456:
			return 400;	
		case 7455:
			return 250;	
		case 7454:
			return 50;	
		case 7452:
			return 250;
		case 23659:
			return 100000;
		case 18349:
		case 18351:
		case 18353:
		case 18355:
		case 18357:
			return 200000;
		case 3024:
		case 3025:
			return 15000;	
		case 4153:
			return 3500000;
		case 11690:
			return 20000000;
		case 18359:
			return 100000;	
		case 19784:
			return 2000;	
		case 21787:
			return 1800000;	
		case 11730:
			return 65000000;	
		case 11702:
			return 500000000;
		case 11708:
			return 300000000;	
		case 11704:
			return 200000000;
		case 11706:
			return 400000000;
		case 15020:
		case 15017:
		case 15220:
		case 15018:
		case 15019:
			return 3000;
		case 22215:
		case 22216:
		case 22217:
		case 22218:
			return 500000;
		case 9470:
			return 30000;
		case 1609://opal
			return 5000;
		case 1611://jade
			return 6000;
		case 1613://red topaz
			return 7000;	
		case 1607://sapphire
			return 8000;
		case 1605://emerald
			return 9000;
		case 1603://ruby
			return 10000;
		case 1601://diamond
			return 11000;
		case 1615://dragonstone
			return 12000;
		case 19780://Jericho sword (no spec attk)
			return 330000000;
		case 6055://weeds
			return 5000;	
		case 6585://amulet of fury
			return 7500000;	
		case 21793://ragefire boots
			return 1800000;	
		case 1663://diamond necklace
			return 300000;	
		case 961:// planks
			return 2500;
		case 8779:// oak planks
			return 5000;	
		case 8781:// teak planks
			return 5000;	
		case 8783:// mahogany planks
			return 5000;	
		case 1464:// archery tickets
			return 100000000;
		case 7651://silver dust
			return 175000;	
		case 1636://gold ring
			return 125000;
		case 1740://cowhide
			return 3500;
		case 951://silk
			return 1500;	
		case 6306://dragonstone
			return 50000000;
		case 773://dragonstone
			return 2147111111;
		case 1050:
			return 2000;
		case 962:
			return 100000000;
		case 24433:
			return 1000000000;
		case 23698:
		case 23699:
		case 23697:
		case 23700:
			return 100000000;
		case 1052:
			return 25000;
		case 21467:
		case 21468:
		case 21469:
		case 21470:
		case 21471:
			return 150000;
		case 28004:
			return 10000;	
		case 24437:
			return 1000000;
		case 1038:
		case 1040:
		case 1042:
		case 1044:
		case 1046:
		case 1048:
			return 2000;
		case 7671:
		case 7673:
			return 15;
		case 5608:
		case 5609:
		case 5607:
		case 6666:
			return 10;
		case 1053:
		case 1055:
		case 1057:
			return 150;
		case 24977:
		case 24983:
		case 24974:
		case 24989:
		case 24980:
		case 24986:
			return 200000000;
		case 25654:
		case 25664:
			return 250000000;
		case 19336:
		case 19337:
		case 19338:
		case 19340:
		case 19341:
		case 19342:
		case 19343:
		case 19345:
		case 23692:
		case 23693:
		case 23694:
		case 23695:
		case 23696:
			return 23500000;
		case 1350:
		case 1348:
		case 10346:
		case 10352:
		case 19314:
		case 19317:
		case 19320:
		case 19308:
		case 19311:
			return 350000000;
		case 20159:
		case 20163:
		case 20167:
		case 20147:
		case 20151:
		case 20155:
		case 20135:
		case 20139:
		case 20143:
			return 500000000;
		case 25022:
		case 11724:
		case 11726:
		case 25019:
		case 25025:
		case 11728:
		case 19364:
		case 11718:
		case 11720:
		case 11722:
		case 25013:
		case 25016:
		case 25010:
		case 25037:
		case 11694:
		case 11696:
		case 11698:
		case 11700:
			return 100000000;
		//pvp shop
		case 10547:
			return 150;
		}
		return 1;
	}
	
	public static int getSellPrice(int itemId) {
		for (int i = 0; i < values.length; i++) {
			if (ItemManager.values[i] == null) {
				continue;
			}
			if (ItemManager.values[i].itemId == itemId) {
				return values[i].value;
			}
		}
		switch (itemId) {
				case 9772:	
		case 9778:
		case 9784:
		case 9790:
		case 9796:
		case 9802:
		case 9808:
		case 9949:
		case 9814:
		case 10662:
		case 10647:
		case 10648:
		case 10649:
		case 10650:
		case 10651:
		case 10652:
		case 10653:
		case 10654:
		case 10655:
		case 10656:
		case 10657:
		case 10658:
		case 10659:
		case 10660:
		case 9948:
		case 9950:	
		case 9812:
		case 9809:
		case 9806:
		case 9800:
		case 9794:
		case 9788:
		case 9785:
		case 9782:
		case 9779:
		case 9776:
		case 9770:
		case 9774:
		case 9771:
		case 9780:
		case 9786:
		case 9792:
		case 9798:
		case 9804:
		case 9810:
			return 99000;
		case 989:
			return 7500000;
		case 23745:
			return 2500000;	
		case 23746:
			return 3000000;	
		case 23747:
			return 4000000;	
		case 23748:
			return 5000000;    
	    case 15243:
			return 500;
			case 29016:
			return 500000000;
		case 29017:
			return 250000000;
		case 29018:
			return 50000000;
		case 29019:
			return 10000000;
		case 29020:
			return 1000000;
		case 29021:
			return 1000000000;			
		case 24977:
		case 24983:
		case 24974:
		case 24989:
		case 24980:
		case 24986:
			return 200000000;
		case 25654:
		case 25664:
			return 250000000;	
		case 19336:
		case 19337:
		case 19338:
		case 19340:
		case 19341:
		case 19342:
		case 19343:
		case 19345:
		case 19335:
		case 23692:
		case 23693:
		case 23694:
		case 23695:
		case 23696:
			return 23500000;	
		case 1350:
		case 1348:
		case 10346:
		case 10352:
		case 19314:
		case 19317:
		case 19320:
		case 19308:
		case 19311:
			return 350000000;
		case 1464:
		return 100000000;
		case 25022:
		case 11724:
		case 11726:
		case 25019:
		case 25025:
		case 11728:
		case 19364:
		case 19394:
		case 19370:
		case 11718:
		case 11720:
		case 11722:
		case 25013:
		case 25016:
		case 25010:
		case 25037:
		case 19392:
		case 19368:
		case 11694:
		case 11696:
		case 11698:
		case 11700:
			return 100000000;	
		case 24359:
		case 24360:
		case 24363:
		case 24361:
		case 24362:
			return 1000000;	
		case 24100:
		case 24102:
		case 24104:
		case 24106:
			return 50000;	
		case 24354:
		case 24356:
		case 24357:
		case 24358:
			return 500000;	
		case 6137:
        case 6139:
		case 6141:
		case 6153:
		case 6147:
			return 500000;
		case 13876:
		case 13870:
		case 13873:
			return 1000000;	
		case 24437:
			return 1000000;	
		case 21467:
		case 21468:
		case 21469:
		case 21470:
		case 21471:
			return 150000;	
		case 21472:
		case 21473:
		case 21474:
		case 21475:
		case 21476:
			return 50000;	
		case 15017:
			return 50000;	
		case 18744:
		case 18745:
		case 18746:
			return 20;	
		case 28004:
			return 10000;	
		case 9470:
			return 30000;	
		case 13734:
		    return 40000000;    
		case 22215:
		case 22216:
		case 22217:
		case 22218:
			return 500000;
		case 20949:
		case 20950:
		case 20951:
		case 20952:
			return 10;
		case 13101:
			return 50000;
		case 13672:
		case 13673:
		case 13674:
		case 13675:
			return 500000;
		case 11702:
		case 13754:
			return 500000000;	
		case 11708:
			return 175000000;
		case 11704:
			return 75000000;
		case 11706:
			return 400000000;
		case 22207:
		case 22209:
		case 22211:
		case 22213:
		    return 10;
		case 14497:
		case 14499:
		case 14501:
		    return 1000000;  
		case 4675:
		case 24092:
		case 24094:
		case 24096:
		case 24098:
		    return 4;    
		case 23531:
			return 80000;
		case 18349:
		case 18351:
		case 18353:
		case 18355:
		case 18357:
			return 200000;	
		case 18359:
			return 100000;
		case 23621:
		case 23351:
		case 6685:
			return 10000;	
		case 15444:	
	    case 22362:
		case 24108:
		case 24110:
		case 24112:
		case 24114:
			return 100000;
			case 804:
			return 750;			
		case 805:
			return 1050;
		case 806:
			return 10;			
		case 10498:
		case 10499:
			return 1000000;	
		case 5680:
			return 32000;	
		case 5698:
			return 62000;	
		case 10348:
		case 10350:
		return 350000000;
        case 10330:
        case 10332:
        case 10334:
        case 10338:
        case 10340:
        case 10342:
			return 500000;	
		case 18335:
			return 50000;
		case 22486:
		case 22490:
		case 22482:
		case 22494:
			return 10000;	
		case 21777:
		case 2581:
		   	return 15000000;	
		case 324:
		case 1386:
			return 175000;	
		case 8848:
		case 8847:
		case 8846:
		case 8845:
		case 8844:
			return 50000;
		case 2412:
		case 2413:
		case 2414:
			return 5000;	
		case 22470:
		case 22474:
		case 22478:
			return 7500;	
		case 21790:
			return 1;	
		case 6746:
			return 25000000;	
		case 12196:
        case 21512:
        case 22992:
        case 24511:
        case 24512:
			return 1;	
		case 4722:
		case 4720:
		case 4718:
		case 4716:
		case 4753:
		case 4755:
		case 4757:
		case 4759:
		case 4724:
		case 4726:
		case 4728:
		case 4730:
		case 4745:
		case 4747:
		case 4749:
		case 4751:
		case 4712:
		case 4714:
		case 4710:
		case 4708:
		case 4736:
		case 4738:
		case 4732:
		case 4734:
			return 15000000;	
		case 2572:
			return 500000;	
		case 20671:
			return 30000;	
		case 6889:
			return 4500000;
		case 15126:
			return 250000;
		case 9244:
			return 6500;	
		case 10008:
		case 10006:
		    return 10000;	
		case 11235:
			return 2500000;	
		case 15486:
			return 10000000;	
		case 6918:
			return 500000;
		case 6920:
			return 500000;
		case 6916:
			return 500000;	
		case 6922:
			return 500000;	
		case 6924:
			return 500000;	
		case 11335:
			return 15000000;
		case 14479:
			return 15000000;
		case 1187:
			return 250000;
		case 4151:
			return 10000000;	
		case 21371:
			return 55000000;	
		case 4153:
			return 250000;
		case 6914:
			return 10000000;	
		case 8839:
		case 8840:
			return 1000;
		case 8842:
			return 750;	
		case 10611:
		case 11663:
		case 11664:
		case 11665:
			return 975;	
		case 6055://weeds
			return 250;
		case 6585://amulet of fury
			return 7500000;
		case 21793://ragefire boots
			return 1;	
		case 1662://diamond necklace
			return 35000;	
		case 7650://silver dust
			return 22000;
		case 1635://gold ring
			return 15000;	
		case 1739://cowhide
			return 7500;
		case 950://silk
			return 2500;
		case 6306://dragonstone
			return 50000000;
		case 773://dragonstone
			return 2147111111;	
		case 23698:
		case 23699:
		case 23697:
		case 23700:
			return 100000000;	
		case 1052:
			return 25000;
		case 1053:
		case 1055:
		case 1057:
			return 150;

		}
		return 1;
	}
	
}
