return_to_table(N, []) :- true, !.
return_to_table(N, [H | T]) :- assert(have_prime(N, H)), return_to_table(N, T), !.

prime_factors(N, Factors) :- \+ number(N), list(Factors), prod(Factors, P), N is P, sorted(Factors), all_prime(Factors).
prime_factors(N, Factors) :- number(N), findall(Factors, have_prime(N, Factors), Y), add_rest(N, Y, Factors).

add_rest(1, [], []).
add_rest(N, [], [N]) :- prime(N).
add_rest(N, [H1 | T1], [H2 | T2]) :- N mod H1 =:= 0, H2 is H1, N1 is div(N, H1), add_rest(N1, [H1 | T1], T2).
add_rest(N, [H1 | T1], A) :- N mod H1 =\= 0, add_rest(N, T1, A).

sieve(MAX_N, I) :- I * I > MAX_N.
sieve(MAX_N, I) :- I * I =< MAX_N, \+ have_prime(I, X), I1 is I + 2, add_prime(MAX_N, I, I), sieve(MAX_N, I1).
sieve(MAX_N, I) :- I * I =< MAX_N, have_prime(I, X), I1 is I + 2, sieve(MAX_N, I1).

add_prime(MAX_N, START, JUMP) :- START > MAX_N.
add_prime(MAX_N, START, JUMP) :- START =< MAX_N, assert(have_prime(START, JUMP)), S1 is START + JUMP, add_prime(MAX_N, S1, JUMP).

init(MAX_N) :- add_prime(MAX_N, 2, 2), sieve(MAX_N, 3).

sorted([]).
sorted([_]).
sorted([X,Y|T]) :- X =< Y, sorted([Y|T]).

all_prime(Numbers) :- forall(member(X, Numbers), prime(X)).

prod([], 1).
prod([H | T], R) :- prod(T, R1), R is H * R1.

prime(N) :- N =\= 1, (\+ have_prime(N, X); have_prime(N, N)).
composite(N) :- N =\= 1, have_prime(N, X), X < N.
prime_divisors(N, Divisors) :- prime_factors(N, Divisors).

find_divisors_cycle(N, [], [], D, STEP) :- D * D > N.
find_divisors_cycle(N, [H], [], D, STEP) :- D * D =:= N, H is D.
find_divisors_cycle(N, [H1 | T1], [H2 | T2], D, STEP) :- D * D < N, N mod D =:= 0, H1 is D, H2 is div(N, D), D1 is D + STEP, find_divisors_cycle(N, T1, T2, D1, STEP).
find_divisors_cycle(N, SmallDivisors, BigDivisors, D, STEP) :- D * D < N, N mod D =\= 0, D1 is D + STEP, find_divisors_cycle(N, SmallDivisors, BigDivisors, D1, STEP).

find_divisors(N, R) :- N mod 2 =:= 0, find_divisors_cycle(N, SmallDivisors, BigDivisors, 1, 1), reverse(BigDivisors, T), append(SmallDivisors, T, R).
find_divisors(N, R) :- N mod 2 =\= 0, find_divisors_cycle(N, SmallDivisors, BigDivisors, 1, 2), reverse(BigDivisors, T), append(SmallDivisors, T, R).

div_div([], []).
div_div([H_D | T_D], [H_R | T_R]) :- prime_factors(H_D, H_R), div_div(T_D, T_R).

divisors_divisors(N, ResultList) :- find_divisors(N, Divisors), div_div(Divisors, ResultList).

% generic123