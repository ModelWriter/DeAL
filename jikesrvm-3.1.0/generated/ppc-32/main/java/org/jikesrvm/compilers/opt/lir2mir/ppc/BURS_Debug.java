package org.jikesrvm.compilers.opt.lir2mir.ppc; 
public class BURS_Debug {
public static final String[] string = {
	null,     	// 0
	"stm: r",  	// 1
	"r: REGISTER",  	// 2
	"r: czr",  	// 3
	"r: rs",  	// 4
	"r: rz",  	// 5
	"rs: rp",  	// 6
	"rz: rp",  	// 7
	"any: NULL",  	// 8
	"any: r",  	// 9
	"any: ADDRESS_CONSTANT",  	// 10
	"any: INT_CONSTANT",  	// 11
	"any: LONG_CONSTANT",  	// 12
	"any: OTHER_OPERAND(any,any)",  	// 13
	"stm: RESOLVE",  	// 14
	"stm: IG_PATCH_POINT",  	// 15
	"stm: UNINT_BEGIN",  	// 16
	"stm: UNINT_END",  	// 17
	"stm: YIELDPOINT_PROLOGUE",  	// 18
	"stm: YIELDPOINT_EPILOGUE",  	// 19
	"stm: YIELDPOINT_BACKEDGE",  	// 20
	"stm: LOWTABLESWITCH(r)",  	// 21
	"stm: NOP",  	// 22
	"r: GUARD_MOVE",  	// 23
	"r: GUARD_COMBINE",  	// 24
	"stm: NULL_CHECK(r)",  	// 25
	"r: GET_CAUGHT_EXCEPTION",  	// 26
	"stm: SET_CAUGHT_EXCEPTION(r)",  	// 27
	"stm: WRITE_FLOOR",  	// 28
	"stm: READ_CEILING",  	// 29
	"stm: DCBF(r)",  	// 30
	"stm: DCBST(r)",  	// 31
	"stm: DCBT(r)",  	// 32
	"stm: DCBTST(r)",  	// 33
	"stm: DCBZ(r)",  	// 34
	"stm: DCBZL(r)",  	// 35
	"stm: ICBI(r)",  	// 36
	"stm: TRAP",  	// 37
	"stm: TRAP_IF(r,r)",  	// 38
	"stm: TRAP_IF(r,INT_CONSTANT)",  	// 39
	"stm: TRAP_IF(r,LONG_CONSTANT)",  	// 40
	"r: BOOLEAN_NOT(r)",  	// 41
	"r: BOOLEAN_CMP_INT(r,INT_CONSTANT)",  	// 42
	"r: BOOLEAN_CMP_INT(r,r)",  	// 43
	"boolcmp: BOOLEAN_CMP_INT(r,INT_CONSTANT)",  	// 44
	"boolcmp: BOOLEAN_CMP_INT(r,r)",  	// 45
	"r: BOOLEAN_CMP_ADDR(r,INT_CONSTANT)",  	// 46
	"r: BOOLEAN_CMP_ADDR(r,r)",  	// 47
	"boolcmp: BOOLEAN_CMP_ADDR(r,INT_CONSTANT)",  	// 48
	"boolcmp: BOOLEAN_CMP_ADDR(r,r)",  	// 49
	"boolcmp: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)",  	// 50
	"boolcmp: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)",  	// 51
	"boolcmp: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)",  	// 52
	"boolcmp: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)",  	// 53
	"r: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)",  	// 54
	"r: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)",  	// 55
	"r: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)",  	// 56
	"r: BOOLEAN_CMP_INT(boolcmp,INT_CONSTANT)",  	// 57
	"r: REF_ADD(r,INT_CONSTANT)",  	// 58
	"r: REF_ADD(r,r)",  	// 59
	"r: REF_ADD(r,REF_MOVE(INT_CONSTANT))",  	// 60
	"r: REF_ADD(r,REF_MOVE(INT_CONSTANT))",  	// 61
	"r: REF_SUB(r,r)",  	// 62
	"r: REF_SUB(INT_CONSTANT,r)",  	// 63
	"r: INT_MUL(r,INT_CONSTANT)",  	// 64
	"r: INT_MUL(r,r)",  	// 65
	"r: INT_DIV(r,r)",  	// 66
	"r: INT_DIV(r,REF_MOVE(INT_CONSTANT))",  	// 67
	"r: INT_REM(r,r)",  	// 68
	"r: INT_REM(r,REF_MOVE(INT_CONSTANT))",  	// 69
	"r: REF_NEG(r)",  	// 70
	"rz: INT_SHL(r,INT_CONSTANT)",  	// 71
	"rz: INT_SHL(r,r)",  	// 72
	"rz: INT_SHL(INT_USHR(r,INT_CONSTANT),INT_CONSTANT)",  	// 73
	"rs: INT_SHR(r,INT_CONSTANT)",  	// 74
	"rs: INT_SHR(r,r)",  	// 75
	"rp: INT_SHR(REF_AND(r,INT_CONSTANT),INT_CONSTANT)",  	// 76
	"rp: INT_USHR(r,INT_CONSTANT)",  	// 77
	"rz: INT_USHR(r,r)",  	// 78
	"rp: INT_USHR(REF_AND(r,INT_CONSTANT),INT_CONSTANT)",  	// 79
	"rp: INT_USHR(REF_AND(r,REF_MOVE(INT_CONSTANT)),INT_CONSTANT)",  	// 80
	"rp: INT_USHR(INT_SHL(r,INT_CONSTANT),INT_CONSTANT)",  	// 81
	"r: REF_AND(r,r)",  	// 82
	"czr: REF_AND(r,INT_CONSTANT)",  	// 83
	"rp: REF_AND(r,INT_CONSTANT)",  	// 84
	"r: REF_AND(REF_NOT(r),REF_NOT(r))",  	// 85
	"r: REF_AND(r,REF_NOT(r))",  	// 86
	"rp: REF_AND(INT_USHR(r,INT_CONSTANT),INT_CONSTANT)",  	// 87
	"rp: REF_AND(INT_USHR(r,INT_CONSTANT),REF_MOVE(INT_CONSTANT))",  	// 88
	"r: REF_OR(r,r)",  	// 89
	"r: REF_OR(r,INT_CONSTANT)",  	// 90
	"r: REF_OR(REF_NOT(r),REF_NOT(r))",  	// 91
	"r: REF_OR(r,REF_NOT(r))",  	// 92
	"r: REF_XOR(r,r)",  	// 93
	"r: REF_XOR(r,INT_CONSTANT)",  	// 94
	"r: REF_NOT(r)",  	// 95
	"r: REF_NOT(REF_OR(r,r))",  	// 96
	"r: REF_NOT(REF_AND(r,r))",  	// 97
	"r: REF_NOT(REF_XOR(r,r))",  	// 98
	"r: FLOAT_ADD(r,r)",  	// 99
	"r: DOUBLE_ADD(r,r)",  	// 100
	"r: FLOAT_MUL(r,r)",  	// 101
	"r: DOUBLE_MUL(r,r)",  	// 102
	"r: FLOAT_SUB(r,r)",  	// 103
	"r: DOUBLE_SUB(r,r)",  	// 104
	"r: FLOAT_DIV(r,r)",  	// 105
	"r: DOUBLE_DIV(r,r)",  	// 106
	"r: FLOAT_NEG(r)",  	// 107
	"r: DOUBLE_NEG(r)",  	// 108
	"r: FLOAT_SQRT(r)",  	// 109
	"r: DOUBLE_SQRT(r)",  	// 110
	"r: FLOAT_ADD(FLOAT_MUL(r,r),r)",  	// 111
	"r: DOUBLE_ADD(DOUBLE_MUL(r,r),r)",  	// 112
	"r: FLOAT_ADD(r,FLOAT_MUL(r,r))",  	// 113
	"r: DOUBLE_ADD(r,DOUBLE_MUL(r,r))",  	// 114
	"r: FLOAT_SUB(FLOAT_MUL(r,r),r)",  	// 115
	"r: DOUBLE_SUB(DOUBLE_MUL(r,r),r)",  	// 116
	"r: FLOAT_NEG(FLOAT_ADD(FLOAT_MUL(r,r),r))",  	// 117
	"r: DOUBLE_NEG(DOUBLE_ADD(DOUBLE_MUL(r,r),r))",  	// 118
	"r: FLOAT_NEG(FLOAT_ADD(r,FLOAT_MUL(r,r)))",  	// 119
	"r: DOUBLE_NEG(DOUBLE_ADD(r,DOUBLE_MUL(r,r)))",  	// 120
	"r: FLOAT_NEG(FLOAT_SUB(FLOAT_MUL(r,r),r))",  	// 121
	"r: DOUBLE_NEG(DOUBLE_SUB(DOUBLE_MUL(r,r),r))",  	// 122
	"rs: INT_2BYTE(r)",  	// 123
	"rp: INT_2USHORT(r)",  	// 124
	"rs: INT_2SHORT(r)",  	// 125
	"r: INT_2FLOAT(r)",  	// 126
	"r: INT_2DOUBLE(r)",  	// 127
	"r: FLOAT_2INT(r)",  	// 128
	"r: FLOAT_2DOUBLE(r)",  	// 129
	"r: DOUBLE_2INT(r)",  	// 130
	"r: DOUBLE_2FLOAT(r)",  	// 131
	"r: FLOAT_AS_INT_BITS(r)",  	// 132
	"r: INT_BITS_AS_FLOAT(r)",  	// 133
	"r: REF_MOVE(r)",  	// 134
	"rs: REF_MOVE(INT_CONSTANT)",  	// 135
	"rs: REF_MOVE(INT_CONSTANT)",  	// 136
	"rs: REF_MOVE(INT_CONSTANT)",  	// 137
	"r: FLOAT_MOVE(r)",  	// 138
	"r: DOUBLE_MOVE(r)",  	// 139
	"rs: BYTE_LOAD(r,INT_CONSTANT)",  	// 140
	"rs: BYTE_LOAD(r,r)",  	// 141
	"rp: REF_AND(BYTE_LOAD(r,r),INT_CONSTANT)",  	// 142
	"rp: REF_AND(BYTE_LOAD(r,INT_CONSTANT),INT_CONSTANT)",  	// 143
	"rp: UBYTE_LOAD(r,INT_CONSTANT)",  	// 144
	"rp: UBYTE_LOAD(r,r)",  	// 145
	"rs: SHORT_LOAD(r,INT_CONSTANT)",  	// 146
	"rs: SHORT_LOAD(r,r)",  	// 147
	"rp: USHORT_LOAD(r,INT_CONSTANT)",  	// 148
	"rp: USHORT_LOAD(r,r)",  	// 149
	"r: FLOAT_LOAD(r,INT_CONSTANT)",  	// 150
	"r: FLOAT_LOAD(r,REF_MOVE(ADDRESS_CONSTANT))",  	// 151
	"r: FLOAT_LOAD(r,r)",  	// 152
	"r: DOUBLE_LOAD(r,INT_CONSTANT)",  	// 153
	"r: DOUBLE_LOAD(r,REF_MOVE(ADDRESS_CONSTANT))",  	// 154
	"r: DOUBLE_LOAD(r,r)",  	// 155
	"rs: INT_LOAD(r,INT_CONSTANT)",  	// 156
	"rs: INT_LOAD(r,REF_MOVE(ADDRESS_CONSTANT))",  	// 157
	"rs: INT_LOAD(r,r)",  	// 158
	"rs: INT_LOAD(REF_ADD(r,r),INT_CONSTANT)",  	// 159
	"rs: INT_LOAD(REF_ADD(r,INT_CONSTANT),INT_CONSTANT)",  	// 160
	"stm: BYTE_STORE(r,OTHER_OPERAND(r,INT_CONSTANT))",  	// 161
	"stm: BYTE_STORE(r,OTHER_OPERAND(r,r))",  	// 162
	"stm: BYTE_STORE(INT_2BYTE(r),OTHER_OPERAND(r,INT_CONSTANT))",  	// 163
	"stm: BYTE_STORE(INT_2BYTE(r),OTHER_OPERAND(r,r))",  	// 164
	"stm: SHORT_STORE(r,OTHER_OPERAND(r,INT_CONSTANT))",  	// 165
	"stm: SHORT_STORE(r,OTHER_OPERAND(r,r))",  	// 166
	"stm: SHORT_STORE(INT_2SHORT(r),OTHER_OPERAND(r,INT_CONSTANT))",  	// 167
	"stm: SHORT_STORE(INT_2SHORT(r),OTHER_OPERAND(r,r))",  	// 168
	"stm: SHORT_STORE(INT_2USHORT(r),OTHER_OPERAND(r,INT_CONSTANT))",  	// 169
	"stm: SHORT_STORE(INT_2USHORT(r),OTHER_OPERAND(r,r))",  	// 170
	"stm: INT_STORE(r,OTHER_OPERAND(r,INT_CONSTANT))",  	// 171
	"stm: INT_STORE(r,OTHER_OPERAND(r,REF_MOVE(ADDRESS_CONSTANT)))",  	// 172
	"stm: INT_STORE(r,OTHER_OPERAND(r,r))",  	// 173
	"stm: INT_STORE(r,OTHER_OPERAND(REF_ADD(r,INT_CONSTANT),INT_CONSTANT))",  	// 174
	"stm: FLOAT_STORE(r,OTHER_OPERAND(r,INT_CONSTANT))",  	// 175
	"stm: FLOAT_STORE(r,OTHER_OPERAND(r,REF_MOVE(ADDRESS_CONSTANT)))",  	// 176
	"stm: FLOAT_STORE(r,OTHER_OPERAND(r,r))",  	// 177
	"stm: DOUBLE_STORE(r,OTHER_OPERAND(r,INT_CONSTANT))",  	// 178
	"stm: DOUBLE_STORE(r,OTHER_OPERAND(r,REF_MOVE(ADDRESS_CONSTANT)))",  	// 179
	"stm: DOUBLE_STORE(r,OTHER_OPERAND(r,r))",  	// 180
	"stm: INT_IFCMP(r,r)",  	// 181
	"stm: INT_IFCMP(r,INT_CONSTANT)",  	// 182
	"stm: INT_IFCMP(INT_2BYTE(r),INT_CONSTANT)",  	// 183
	"stm: INT_IFCMP(INT_2SHORT(r),INT_CONSTANT)",  	// 184
	"stm: INT_IFCMP(INT_USHR(r,r),INT_CONSTANT)",  	// 185
	"stm: INT_IFCMP(INT_SHL(r,r),INT_CONSTANT)",  	// 186
	"stm: INT_IFCMP(INT_SHR(r,r),INT_CONSTANT)",  	// 187
	"stm: INT_IFCMP(INT_USHR(r,INT_CONSTANT),INT_CONSTANT)",  	// 188
	"stm: INT_IFCMP(INT_SHL(r,INT_CONSTANT),INT_CONSTANT)",  	// 189
	"stm: INT_IFCMP(INT_SHR(r,INT_CONSTANT),INT_CONSTANT)",  	// 190
	"stm: INT_IFCMP(REF_AND(r,INT_CONSTANT),INT_CONSTANT)",  	// 191
	"stm: INT_IFCMP(boolcmp,INT_CONSTANT)",  	// 192
	"stm: INT_IFCMP(boolcmp,INT_CONSTANT)",  	// 193
	"stm: INT_IFCMP(boolcmp,INT_CONSTANT)",  	// 194
	"stm: INT_IFCMP(boolcmp,INT_CONSTANT)",  	// 195
	"stm: INT_IFCMP2(r,r)",  	// 196
	"stm: INT_IFCMP2(r,INT_CONSTANT)",  	// 197
	"stm: FLOAT_IFCMP(r,r)",  	// 198
	"stm: DOUBLE_IFCMP(r,r)",  	// 199
	"stm: FLOAT_CMPL(r,r)",  	// 200
	"stm: FLOAT_CMPG(r,r)",  	// 201
	"stm: DOUBLE_CMPL(r,r)",  	// 202
	"stm: DOUBLE_CMPG(r,r)",  	// 203
	"stm: GOTO",  	// 204
	"stm: RETURN(NULL)",  	// 205
	"stm: RETURN(r)",  	// 206
	"r: CALL(r,any)",  	// 207
	"r: CALL(BRANCH_TARGET,any)",  	// 208
	"r: SYSCALL(r,any)",  	// 209
	"r: GET_TIME_BASE",  	// 210
	"r: OTHER_OPERAND(r,r)",  	// 211
	"r: YIELDPOINT_OSR(any,any)",  	// 212
	"r: PREPARE_INT(r,r)",  	// 213
	"r: PREPARE_LONG(r,r)",  	// 214
	"r: ATTEMPT_INT(r,r)",  	// 215
	"r: ATTEMPT_LONG(r,r)",  	// 216
	"stm: IR_PROLOGUE",  	// 217
	"r: LONG_ADD(r,r)",  	// 218
	"r: LONG_SUB(r,r)",  	// 219
	"r: LONG_MUL(r,r)",  	// 220
	"r: LONG_NEG(r)",  	// 221
	"r: LONG_SHL(r,r)",  	// 222
	"r: LONG_SHL(r,INT_CONSTANT)",  	// 223
	"r: LONG_SHR(r,r)",  	// 224
	"r: LONG_SHR(r,INT_CONSTANT)",  	// 225
	"r: LONG_USHR(r,r)",  	// 226
	"r: LONG_USHR(r,INT_CONSTANT)",  	// 227
	"r: LONG_AND(r,r)",  	// 228
	"r: LONG_OR(r,r)",  	// 229
	"r: LONG_XOR(r,r)",  	// 230
	"r: LONG_NOT(r)",  	// 231
	"r: INT_2LONG(r)",  	// 232
	"r: LONG_2INT(r)",  	// 233
	"r: DOUBLE_AS_LONG_BITS(r)",  	// 234
	"r: LONG_BITS_AS_DOUBLE(r)",  	// 235
	"r: LONG_MOVE(LONG_CONSTANT)",  	// 236
	"r: LONG_MOVE(r)",  	// 237
	"stm: LONG_CMP(r,r)",  	// 238
	"stm: LONG_IFCMP(r,r)",  	// 239
	"stm: INT_IFCMP(ATTEMPT_INT(r,r),INT_CONSTANT)",  	// 240
	"stm: INT_IFCMP(ATTEMPT_ADDR(r,r),INT_CONSTANT)",  	// 241
	"stm: INT_IFCMP(REF_NEG(r),INT_CONSTANT)",  	// 242
	"stm: INT_IFCMP(REF_NOT(r),INT_CONSTANT)",  	// 243
	"stm: INT_IFCMP(REF_ADD(r,r),INT_CONSTANT)",  	// 244
	"stm: INT_IFCMP(REF_AND(r,r),INT_CONSTANT)",  	// 245
	"stm: INT_IFCMP(REF_OR(r,r),INT_CONSTANT)",  	// 246
	"stm: INT_IFCMP(REF_XOR(r,r),INT_CONSTANT)",  	// 247
	"stm: INT_IFCMP(REF_AND(r,REF_MOVE(INT_CONSTANT)),INT_CONSTANT)",  	// 248
	"stm: INT_IFCMP(REF_AND(r,REF_MOVE(INT_CONSTANT)),INT_CONSTANT)",  	// 249
	"stm: INT_IFCMP(REF_ADD(r,INT_CONSTANT),INT_CONSTANT)",  	// 250
	"stm: INT_IFCMP(REF_AND(r,REF_NOT(r)),INT_CONSTANT)",  	// 251
	"stm: INT_IFCMP(REF_OR(r,REF_NOT(r)),INT_CONSTANT)",  	// 252
	"czr: REF_AND(r,REF_MOVE(INT_CONSTANT))",  	// 253
	"r: REF_AND(r,REF_MOVE(INT_CONSTANT))",  	// 254
	"r: REF_OR(r,REF_MOVE(INT_CONSTANT))",  	// 255
	"r: REF_OR(r,REF_MOVE(INT_CONSTANT))",  	// 256
	"r: REF_XOR(r,REF_MOVE(INT_CONSTANT))",  	// 257
	"r: REF_MOVE(ADDRESS_CONSTANT)",  	// 258
	"r: REF_MOVE(ADDRESS_CONSTANT)",  	// 259
	"r: REF_MOVE(ADDRESS_CONSTANT)",  	// 260
	"r: LONG_LOAD(r,INT_CONSTANT)",  	// 261
	"r: LONG_LOAD(r,REF_MOVE(ADDRESS_CONSTANT))",  	// 262
	"r: LONG_LOAD(r,r)",  	// 263
	"stm: LONG_STORE(r,OTHER_OPERAND(r,INT_CONSTANT))",  	// 264
	"stm: LONG_STORE(r,OTHER_OPERAND(r,REF_MOVE(ADDRESS_CONSTANT)))",  	// 265
	"stm: LONG_STORE(r,OTHER_OPERAND(r,r))",  	// 266
	"r: PREPARE_ADDR(r,r)",  	// 267
	"r: ATTEMPT_ADDR(r,r)",  	// 268
};

}
