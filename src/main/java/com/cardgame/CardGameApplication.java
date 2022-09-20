package com.cardgame;

import com.cardgame.Service.RoomService;
import com.cardgame.Dto.requests.gamelogic.Winninghandrequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
public class CardGameApplication {


	public static void main(String[] args) {

//		thewinninghand();
//		generateNumbers(1);
		SpringApplication.run(CardGameApplication.class, args);

	}
	public static void thewinninghand(){

  ArrayList<ArrayList<Winninghandrequest>> arrayLists=new ArrayList<>();
		arrayLists.add(new ArrayList<>(0));
		arrayLists.add(new ArrayList<>(0));
		
		RoomService roomService=new RoomService(null,null,null,null,null,null,null,null,null,null, null);
//         roomService.winninghand(arrayLists,null);

	}
	public static List<Integer> generateNumbers(int count) {
		List<Integer> numbers = IntStream.range(11, count + 1 )
				.boxed()
				.collect(Collectors.toList());
		numbers.addAll(numbers);
		Collections.shuffle(numbers);
		System.out.println(numbers);
		return numbers;
	}

}
