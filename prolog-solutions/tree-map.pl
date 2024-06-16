get_height(nil, 0).
get_height(node(K, V, H, L, R), H).

get_balance(node(K, V, H, L, R), Bal) :-
	get_height(L, LeftHeight),
	get_height(R, RightHeight),
	Bal is RightHeight - LeftHeight.

recalc_height(L, R, NewHeight) :- get_height(L, LH), get_height(R, RH), get_max(LH, RH, M), NewHeight is M + 1.

get_max(A, B, R) :- A > B, R = A.
get_max(A, B, R) :- A =< B, R = B.

rotate_right(node(K, V, H, node(KW, VW, HQ, A, B), C), node(KW, VW, HW, A, node(K, V, PHeight, B, C))) :-
	recalc_height(B, C, PHeight),
	recalc_height(A, node(K, V, PHeight, B, C), HW).

rotate_left(node(K, V, H, A, node(KW, VW, HP, B, C)), node(KW, VW, HW, node(K, V, QHeight, A, B), C)) :-
    recalc_height(A, B, QHeight),
    recalc_height(C, node(K, V, QHeight, A, B), HW).

balance(node(K, V, H, L, R), Result) :-
	get_balance(node(K, V, H, L, R), Bal1),
	get_balance(L, Bal2),
	Bal1 == -2,
	Bal2 =< 0,
	recalc_height(L, R, HW),
	rotate_right(node(K, V, HW, L, R), Result).

balance(node(K, V, H, L, R), Result) :-
	get_balance(node(K, V, H, L, R), Bal1),
	get_balance(L, Bal2),
	Bal1 == -2,
	Bal2 > 0,
	recalc_height(L, R, HW),
	rotate_left(L, TMP),
	rotate_right(node(K, V, HW, TMP, R), Result).

balance(node(K, V, H, L, R), Result) :-
	get_balance(node(K, V, H, L, R), Bal1),
	get_balance(L, Bal2),
	Bal1 == 2,
	Bal2 >= 0,
	recalc_height(L, R, HW),
	rotate_left(node(K, V, HW, L, R), Result).

balance(node(K, V, H, L, R), Result) :-
	get_balance(node(K, V, H, L, R), Bal1),
	get_balance(L, Bal2),
	Bal1 == 2,
	Bal2 < 0,
	recalc_height(L, R, HW),
	rotate_right(R, TMP),
	rotate_left(node(K, V, HW, L, TMP), Result).

balance(R, R) :-
	get_balance(R, Bal),
	Bal \== 2, Bal \== -2.

map_put_decorator(nil, KeyPut, ValuePut, node(KeyPut, ValuePut, 1, nil, nil)).

map_put_decorator(node(Key, Value, Height, Left, Right), KeyPut, ValuePut, Result) :-
	KeyPut < Key,
	map_put_decorator(Left, KeyPut, ValuePut, LeftWill),
	recalc_height(LeftWill, Right, HeightWill),
	balance(node(Key, Value, HeightWill, LeftWill, Right), Result).

map_put_decorator(node(Key, Value, Height, Left, Right), KeyPut, ValuePut, Result) :-
	KeyPut > Key,
	map_put_decorator(Right, KeyPut, ValuePut, RightWill),
	recalc_height(Left, RightWill, HeightWill),
	Result = node(Key, Value, HeightWill, Left, RightWill).

map_build([], nil).
map_build([(K, V) | T], TreeMap) :- map_build(T, TMP), map_put(TMP, K, V, TreeMap).

map_get(node(Key, Value, Height, Left, Right), Key, Value).

map_get(node(Key, Value, Height, Left, Right), KeyFind, Result) :-
	KeyFind < Key,
	map_get(Left, KeyFind, Result).

map_get(node(Key, Value, Height, Left, Right), KeyFind, Result) :-
	KeyFind > Key,
	map_get(Right, KeyFind, Result).

map_remove_decorator(node(Key, Value, Height, Left, Right), KeyRemove, node(Key, Value, HeightWill, LeftWill, Right)) :-
	KeyRemove < Key,
	Left \== nil,
	map_remove_decorator(Left, KeyRemove, LeftWill),
	recalc_height(LeftWill, Right, HeightWill).

map_remove_decorator(node(Key, Value, Height, Left, Right), KeyRemove, node(Key, Value, HeightWill, Left, RightWill)) :-
	KeyRemove > Key,
	Right \== nil,
	map_remove_decorator(Right, KeyRemove, RightWill),
	recalc_height(Left, RightWill, HeightWill), !.

map_remove_decorator(node(Key, Value, Height, nil, Right), Key, Right) :- !.

map_remove_decorator(node(Key, Value, Height, Left, Right), Key, node(KeyWill, ValueWill, HeightWill, LeftWill, Right)) :-
	Left \== nil,
	find_and_remove_rightmost(Left, (KeyWill, ValueWill), LeftWill),
	recalc_height(LeftWill, Right, HeightWill), !.

map_remove(Root, Key, R) :- map_get(Root, Key, X), map_remove_decorator(Root, Key, R).
map_remove(Root, Key, Root) :- \+ map_get(Root, Key, X).

map_put(Root, Key, Value, R) :-
	map_remove(Root, Key, X),
	map_put_decorator(X, Key, Value, R).

find_and_remove_rightmost(node(Key, Value, Height, Left, nil), (Key, Value), Left).
find_and_remove_rightmost(node(Key, Value, Height, Left, Right), R, Result) :-
	Right \== nil,
	find_and_remove_rightmost(Right, R, RightWill),
	recalc_height(Left, RightWill, HeightWill),
	Result = node(Key, Value, HeightWill, Left, RightWill).
	% balance(node(Key, Value, HeightWill, Left, RightWill), Result).

map_getCeiling(nil, Key, nil, nil) :- !.

map_getCeiling(node(K, V, H, L, R), Key, ResKey, ResValue) :-
    Key > K,
    map_getCeiling(R, Key, ResKey, ResValue), !.

map_getCeiling(node(K, V, H, L, R), Key, ResKey, ResValue) :-
    Key =< K,
    map_getCeiling(L, Key, MBResKey, MBResValue),
    (MBResKey \== nil ->
        (ResKey = MBResKey, ResValue = MBResValue) ;
        (ResKey = K, ResValue = V)
    ), !.

map_getCeiling(Root, Key, ResultValue) :-
    map_getCeiling(Root, Key, _, ResultValue),
    ResultValue \== nil.

map_putCeiling(Root, Key, Value, Root) :- map_getCeiling(Root, Key, nil, nil).
map_putCeiling(Root, Key, Value, Result) :-
    map_getCeiling(Root, Key, ResKey, ResValue),
    ResKey \== nil,
    map_remove(Root, ResKey, Root1),
    map_put(Root1, ResKey, Value, Result).