/*
 * Copyright 2003-2005 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 *
 */

inline void Assembler::emit_long64(jlong x) {
  *(jlong*) _code_pos = x;
  _code_pos += sizeof(jlong);
  code_section()->set_end(_code_pos);
}

inline void MacroAssembler::pd_patch_instruction(address branch, address target) {
  unsigned char op = branch[0];
  assert(op == 0xE8 /* call */ ||
         op == 0xE9 /* jmp */ ||
         op == 0xEB /* short jmp */ ||
         (op & 0xF0) == 0x70 /* short jcc */ ||
         op == 0x0F && (branch[1] & 0xF0) == 0x80 /* jcc */,
         "Invalid opcode at patch point");

  if (op == 0xEB || (op & 0xF0) == 0x70) {
    // short offset operators (jmp and jcc)
    char* disp = (char*) &branch[1];
    int imm8 = target - (address) &disp[1];
    guarantee(this->is8bit(imm8), "Short forward jump exceeds 8-bit offset");
    *disp = imm8;
  } else {
    int* disp = (int*) &branch[(op == 0x0F)? 2: 1];
    int imm32 = target - (address) &disp[1];
    *disp = imm32;
  }
}

#ifndef PRODUCT
inline void MacroAssembler::pd_print_patched_instruction(address branch) {
  const char* s;
  unsigned char op = branch[0];
  if (op == 0xE8) {
    s = "call";
  } else if (op == 0xE9 || op == 0xEB) {
    s = "jmp";
  } else if ((op & 0xF0) == 0x70) {
    s = "jcc";
  } else if (op == 0x0F) {
    s = "jcc";
  } else {
    s = "????";
  }
  tty->print("%s (unresolved)", s);
}
#endif // ndef PRODUCT

inline void MacroAssembler::movptr(Address dst, intptr_t src) {
#ifdef _LP64
  Assembler::mov64(dst, src);
#else
  Assembler::movl(dst, src);
#endif // _LP64
}

inline void MacroAssembler::movptr(Register dst, intptr_t src) {
#ifdef _LP64
  Assembler::mov64(dst, src);
#else
  Assembler::movl(dst, src);
#endif // _LP64
}
