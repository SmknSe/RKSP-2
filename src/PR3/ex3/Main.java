package PR3.ex3;

import io.reactivex.rxjava3.core.Observable;

import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static ArrayList<UserFriend> friends = new ArrayList<>();
    public static Random rand = new Random();

    public static Observable<UserFriend> getFriends(int userId) {
        return Observable.fromIterable(friends).filter(friend -> friend.getFriendId() == userId);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 30; i++) {
            friends.add(new UserFriend(rand.nextInt(0, 30), rand.nextInt(0, 30)));
        }

        ArrayList<Integer> uids = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            uids.add(rand.nextInt(0, 30));
        }

        Observable<Integer> uidsStream = Observable.fromIterable(uids);
        Observable<UserFriend> friendObservable = uidsStream.flatMap(id -> getFriends(id));
        friendObservable.subscribe(userFriend ->
                System.out.println("User: " + userFriend.getUserId() + " Friend: " + userFriend.getFriendId()));


    }


}
